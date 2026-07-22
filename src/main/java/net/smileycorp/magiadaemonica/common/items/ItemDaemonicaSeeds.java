package net.smileycorp.magiadaemonica.common.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.IPlantable;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.blocks.DaemonicaBlocks;

public class ItemDaemonicaSeeds extends ItemDaemonica implements IMetaItem {

    public ItemDaemonicaSeeds() {
        super("seeds");
        setHasSubtypes(true);
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        IBlockState state = world.getBlockState(pos);
        if (facing != EnumFacing.UP |! player.canPlayerEdit(pos.offset(facing), facing, stack) |!
                state.getBlock().canSustainPlant(state, world, pos, EnumFacing.UP, (IPlantable)Items.WHEAT_SEEDS)
                |! world.isAirBlock(pos.up())) return EnumActionResult.FAIL;
        if (player instanceof EntityPlayerMP) CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos.up(), stack);
        stack.shrink(1);
        Variant.get(stack.getMetadata()).setBlockState(world, pos);
        return EnumActionResult.SUCCESS;
    }

    @Override
    public String byMeta(int meta) {
        return Variant.get(meta).getName() + "_seeds";
    }

    @Override
    public int getMaxMeta() {
        return Variant.values().length;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        for (int i = 0; i < Variant.values().length; i++) items.add(new ItemStack(this, 1, i));
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + Constants.name(byMeta(stack.getMetadata()));
    }

    public enum Variant {
        SPEARMINT("spearmint", DaemonicaBlocks.SPEARMINT.getDefaultState()),
        WATERMINT("watermint", DaemonicaBlocks.WATERMINT.getDefaultState());

        private final String name;
        private final IBlockState state;

        Variant(String name, IBlockState state) {
            this.name = name;
            this.state = state;
        }

        public String getName() {
            return name;
        }

        public void setBlockState(World world, BlockPos pos) {
            world.setBlockState(pos.up(), state);
        }

        public static Variant get(int meta) {
            return meta < values().length ? values()[meta] : WATERMINT;
        }

    }


}
