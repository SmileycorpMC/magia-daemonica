package net.smileycorp.magiadaemonica.common.blocks;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.smileycorp.magiadaemonica.common.demons.Domain;
import net.smileycorp.magiadaemonica.common.items.DaemonicaItems;
import net.smileycorp.magiadaemonica.common.rituals.RitualsServer;
import net.smileycorp.magiadaemonica.common.rituals.summoning.SummoningCircles;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Random;

public class BlockChalkCandle extends BlockLine implements Lightable, RitualBlock {

    public static final PropertyBool LIT = BlockScentedCandle.LIT;
    public static final PropertyEnum<BlockScentedCandle.Type> TYPE = BlockScentedCandle.TYPE;

    public BlockChalkCandle() {
        super("chalk_candle");
        setDefaultState(blockState.getBaseState().withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false)
                .withProperty(WEST, false).withProperty(LIT, false).withProperty(TYPE, BlockScentedCandle.Type.ROSE));
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NORTH, EAST, SOUTH, WEST, LIT, TYPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(LIT, meta % 2 == 1).withProperty(TYPE, BlockScentedCandle.Type.get(meta/2));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE).ordinal() * 2 + (state.getValue(LIT) ? 1 : 0);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult raytrace, World world, BlockPos pos, EntityPlayer player) {
        return new ItemStack(DaemonicaBlocks.SCENTED_CANDLE, 1, state.getValue(TYPE).ordinal());
    }

    @Override
    public int quantityDropped(Random random) {
        return 1;
    }

    @Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune) {
        return Item.getItemFromBlock(DaemonicaBlocks.SCENTED_CANDLE);
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(TYPE).ordinal();
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(LIT) ? 4 : 0;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return FULL_BLOCK_AABB;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return NULL_AABB;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (!state.getValue(LIT)) return;
        if (world.getTileEntity(pos) != null) return;
        world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, 0, 0, 0);
        if (rand.nextInt(4) == 0) world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.5 + (rand.nextFloat() - 0.5) * 0.05,
                pos.getY() + 0.6, pos.getZ() + 0.5 + (rand.nextFloat() - 0.5) * 0.05, 0, 0, 0);
    }

    @Override
    public boolean isLightable(World world, BlockPos pos, IBlockState state) {
        return !state.getValue(LIT);
    }

    @Override
    public void light(World world, BlockPos pos, IBlockState state) {
        world.setBlockState(pos, state.withProperty(LIT, true));
    }

    @Override
    public int getPowerBonus(World world, BlockPos pos, EntityPlayer player) {
        return world.getBlockState(pos).getValue(LIT) ? 50 : 0;
    }

    @Override
    public EnumMap<Domain, Integer> getAffiliationBonus(World world, BlockPos pos, EntityPlayer player) {
        IBlockState state = world.getBlockState(pos);
        EnumMap<Domain, Integer> map = new EnumMap<>(Domain.class);
        if (state.getValue(LIT)) map.put(state.getValue(TYPE).getDomain(), 1);
        return map;
    }

    @Override
    public String byMeta(int meta) {
        return BlockScentedCandle.Type.get(meta).getName() + "_candle";
    }

    @Override
    public int getMaxMeta() {
        return 7;
    }

    @Override
    public boolean usesCustomItemHandler() {
        return true;
    }

}
