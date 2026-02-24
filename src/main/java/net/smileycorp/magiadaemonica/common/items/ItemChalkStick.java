package net.smileycorp.magiadaemonica.common.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.common.blocks.BlockScentedCandle;
import net.smileycorp.magiadaemonica.common.blocks.DaemonicaBlocks;

public class ItemChalkStick extends ItemDaemonicaEdible {

    public ItemChalkStick() {
        super("chalk_stick", 1, 0.8f);
        setMaxStackSize(1);
        setMaxDamage(75);
        setAlwaysEdible();
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (side != EnumFacing.UP || player.isSneaking() |! player.canPlayerEdit(pos, side, stack)
                || stack.getItem() != this) return super.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() != DaemonicaBlocks.CHALK_ASH_LINE) {
            if (!world.isSideSolid(pos, EnumFacing.UP)) return super.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
            pos = pos.up();
            state = world.getBlockState(pos);
            if (!state.getBlock().isReplaceable(world, pos) && state.getBlock() != DaemonicaBlocks.SCENTED_CANDLE)
                return super.onItemUse(player, world, pos, hand, side, hitX, hitY, hitZ);
        }
        if (!world.setBlockState(pos, state.getBlock() == DaemonicaBlocks.SCENTED_CANDLE ? DaemonicaBlocks.CHALK_CANDLE.getDefaultState()
                        .withProperty(BlockScentedCandle.TYPE, state.getValue(BlockScentedCandle.TYPE)).withProperty(BlockScentedCandle.LIT, state.getValue(BlockScentedCandle.LIT))
                : DaemonicaBlocks.CHALK_LINE.getDefaultState(), 11)) return EnumActionResult.PASS;
        SoundType sound = SoundType.STONE;
        world.playSound(player, pos, sound.getPlaceSound(), SoundCategory.BLOCKS, (sound.getVolume() + 1) / 2f, sound.getPitch() * 0.8f);
        if (player instanceof EntityPlayerMP) CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
        if (stack.isItemStackDamageable()) stack.damageItem(1, player);
        return EnumActionResult.SUCCESS;
    }

}
