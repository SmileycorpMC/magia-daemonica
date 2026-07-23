package net.smileycorp.magiadaemonica.common.blocks;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.MagiaDaemonica;
import net.smileycorp.magiadaemonica.common.items.ItemPetal;

import java.util.Random;

public class BlockPetals extends BlockBush implements BlockProperties {

    public static final PropertyDirection FACING = BlockHorizontal.FACING;
    public static final PropertyInteger PETAL_AMOUNT = PropertyInteger.create("petal_amount", 1, 4);
    protected static final AxisAlignedBB AABB = new AxisAlignedBB(0, 0.0D, 0, 1, 0.0625D, 1);

    public BlockPetals(String name) {
        super();
        name += "_petals";
        setUnlocalizedName(Constants.name(name));
        setRegistryName(Constants.loc(name));
        setCreativeTab(MagiaDaemonica.CREATIVE_TAB);
        setSoundType(SoundType.PLANT);
        setHardness(0);
        setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(PETAL_AMOUNT, 1));
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, FACING, PETAL_AMOUNT);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(FACING).getIndex() - 2) * 4 + state.getValue(PETAL_AMOUNT) - 1;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(FACING, EnumFacing.values()[meta / 4 + 2]).withProperty(PETAL_AMOUNT, meta % 4 + 1);
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rotation) {
        return state.withProperty(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public IBlockState withMirror(IBlockState state, Mirror mirror) {
        return state.withRotation(mirror.toRotation(state.getValue(FACING)));
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase entity, EnumHand hand) {
        return getDefaultState().withProperty(FACING, entity.getHorizontalFacing().getOpposite());
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return AABB;
    }

    @Override
    public int quantityDropped(IBlockState state, int fortune, Random random) {
        return state.getValue(PETAL_AMOUNT);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking() | !(stack.getItem() instanceof ItemPetal) || ((ItemPetal) stack.getItem()).getBlock() != state.getBlock()) return false;
        int petals = state.getValue(PETAL_AMOUNT);
        if (petals >= 4) return false;
        player.swingArm(hand);
        world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, blockSoundType.getPlaceSound(),
                SoundCategory.BLOCKS, (blockSoundType.getVolume() + 1f) / 2f, blockSoundType.getPitch() * 0.8F);
        world.setBlockState(pos, state.withProperty(PETAL_AMOUNT, petals + 1), 3);
        if (!player.isCreative()) stack.shrink(1);
        if (player instanceof EntityPlayerMP) CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
        return true;
    }

    @Override
    public boolean usesCustomItemHandler() {
        return true;
    }

}
