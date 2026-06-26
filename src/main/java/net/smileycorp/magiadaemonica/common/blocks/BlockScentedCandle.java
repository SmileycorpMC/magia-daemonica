package net.smileycorp.magiadaemonica.common.blocks;

import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.smileycorp.atlas.api.block.BlockBase;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.MagiaDaemonica;
import net.smileycorp.magiadaemonica.common.blocks.tiles.RitualTile;
import net.smileycorp.magiadaemonica.common.demons.Domain;
import net.smileycorp.magiadaemonica.config.BlocksConfig;

import javax.annotation.Nullable;
import java.util.EnumMap;
import java.util.Random;

public class BlockScentedCandle extends BlockBase implements Lightable, RitualBlock {

    public static PropertyBool LIT = PropertyBool.create("lit");
    public static PropertyEnum<Type> TYPE = PropertyEnum.create("type", Type.class);

    public static final AxisAlignedBB AABB = new AxisAlignedBB(0.4375, 0, 0.4375, 0.5625, 0.4375, 0.5625);

    public BlockScentedCandle() {
        super("scented_candle", Constants.MODID, Material.CIRCUITS, SoundType.STONE, 0, 0, 0, MagiaDaemonica.CREATIVE_TAB);
        setDefaultState(blockState.getBaseState().withProperty(LIT, false).withProperty(TYPE, Type.ROSE));
        setTickRandomly(BlocksConfig.rainExtinguishesCandles);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, LIT, TYPE);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(LIT, meta % 2 == 1).withProperty(TYPE, Type.get(meta/2));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(TYPE).ordinal() * 2 + (state.getValue(LIT) ? 1 : 0);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        ItemStack stack = placer.getHeldItem(hand);
        return getDefaultState().withProperty(TYPE, Type.get(stack.getMetadata()));
    }

    @Override
    public int damageDropped(IBlockState state) {
        return state.getValue(TYPE).ordinal();
    }

    @Override
    public boolean canPlaceBlockAt(World world, BlockPos pos) {
        return world.isSideSolid(pos.down(), EnumFacing.UP);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(LIT) ? 4 : 0;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return AABB;
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
    public void randomTick(World world, BlockPos pos, IBlockState state, Random random) {
        if (!state.getValue(LIT) |! world.isRainingAt(pos)) return;
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof RitualTile && ((RitualTile) tile).isActive()) return;
        world.setBlockState(pos, state.withProperty(LIT, false));
        world.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.75f, 1);
        ((WorldServer)world).spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                0, 0, 1, 0, 0.1);
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
    public int getPowerBonus(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return world.getBlockState(pos).getValue(LIT) ? 50 : 0;
    }

    @Override
    public EnumMap<Domain, Integer> getAffiliationBonus(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        EnumMap<Domain, Integer> map = new EnumMap<>(Domain.class);
        if (state.getValue(LIT)) map.put(state.getValue(TYPE).getDomain(), 1);
        return map;
    }

    @Override
    public String byMeta(int meta) {
        return Type.get(meta).getName() + "_candle";
    }

    @Override
    public int getMaxMeta() {
        return 7;
    }

    public enum Type implements IStringSerializable {
        ROSE("rose", Domain.LUST),
        LAVENDER("lavender", Domain.SLOTH),
        LILAC("lilac", Domain.ENVY),
        FRANKINCENSE("frankincense", Domain.PRIDE),
        OAK_ASH("oak_ash", Domain.WRATH),
        PUMPKIN("pumpkin", Domain.GLUTTONY),
        PEPPERMINT("peppermint", Domain.GREED);

        private final String name;
        private final Domain domain;

        Type(String name, Domain domain) {
            this.name = name;
            this.domain = domain;
        }

        @Override
        public String getName() {
            return name;
        }

        public Domain getDomain() {
            return domain;
        }

        public static Type get(int meta) {
            return values()[meta % values().length];
        }

    }

}
