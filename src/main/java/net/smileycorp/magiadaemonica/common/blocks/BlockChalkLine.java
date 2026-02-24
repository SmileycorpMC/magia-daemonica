package net.smileycorp.magiadaemonica.common.blocks;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.ForgeEventFactory;
import net.smileycorp.magiadaemonica.common.items.DaemonicaItems;

public class BlockChalkLine extends BlockLine {

    public BlockChalkLine() {
        super("chalk_line");
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult raytrace, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(DaemonicaItems.CHALK_STICK);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking() |! player.canPlayerEdit(pos, side, stack) || stack.getItem() != Item.getItemFromBlock(DaemonicaBlocks.SCENTED_CANDLE)
                || world.getTileEntity(pos) != null) return super.onBlockActivated(world, pos, state, player, hand, side, hitX, hitY, hitZ);
        if (!world.isRemote) {
            world.captureBlockSnapshots = true;
            if (!world.setBlockState(pos, DaemonicaBlocks.CHALK_CANDLE.getDefaultState().withProperty(BlockScentedCandle.TYPE,
                    BlockScentedCandle.Type.get(stack.getMetadata())), 11)) return false;
            SoundType soundtype = SoundType.STONE;
            world.playSound(null, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1) / 2f, soundtype.getPitch() * 0.8f);
            if (player instanceof EntityPlayerMP) CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
            if (!player.isCreative()) stack.shrink(1);
            player.swingArm(hand);
            ForgeEventFactory.onPlayerBlockPlace(player, world.capturedBlockSnapshots.get(0), side, hand);
            world.captureBlockSnapshots = false;
        }
        return true;
    }


}
