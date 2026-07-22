package net.smileycorp.magiadaemonica.common.blocks;

import net.minecraft.block.BlockCrops;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.MagiaDaemonica;

import java.util.Random;

public class BlockDaemonicaCrop extends BlockCrops implements BlockProperties {

    private static final AxisAlignedBB[] CROPS_AABB = {new AxisAlignedBB(0, 0, 0, 1, 0.125D, 1),
            new AxisAlignedBB(0, 0, 0, 1, 0.25, 1), new AxisAlignedBB(0, 0, 0, 1, 0.375, 1),
            new AxisAlignedBB(0, 0, 0, 1, 0.5, 1), new AxisAlignedBB(0, 0, 0, 1, 0.625, 1), new AxisAlignedBB(0, 0, 0, 1, 0.75D, 1), new AxisAlignedBB(0, 0, 0, 1, 0.875D, 1), new AxisAlignedBB(0, 0, 0, 1, 1, 1)};
    private static PropertyInteger staticProp;
    private final boolean hasItem;
    private DropFunction seed;
    private DropFunction drop;
    private PropertyInteger age;
    private final int maxAge;

    private BlockDaemonicaCrop(String name, int maxAge, boolean hasItem) {
        super();
        setUnlocalizedName(Constants.name(name));
        setRegistryName(Constants.loc(name));
        setCreativeTab(MagiaDaemonica.CREATIVE_TAB);
        setSoundType(SoundType.PLANT);
        setHardness(0);
        this.maxAge = maxAge;
        this.hasItem = hasItem;
    }

    @Override
    protected BlockStateContainer createBlockState() {
        age = staticProp;
        return new BlockStateContainer(this, age);
    }

    @Override
    protected PropertyInteger getAgeProperty() {
        return age;
    }

    @Override
    public int getMaxAge() {
        return maxAge;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
        int age = getAge(state);
        return age < getMaxAge() && seed != null ? seed.get(this, age, 0, world.rand)
                : drop != null ? drop.get(this, age, 0, world.rand) : hasItem ? new ItemStack(this)
                : super.getPickBlock(state, target, world, pos, player);
    }

    @Override
    public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
        return withAge(getMaxAge());
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return new AxisAlignedBB(0, 0, 0, 1, (float)(int)Math.max(16f * (float)state.getValue(age) / (float) getMaxAge(), 2) / 16f, 1);
    }

    @Override
    public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        int age = getAge(state);
        Random rand = world instanceof World ? ((World)world).rand : new Random();
        if (age == getMaxAge()) {
            if (drop != null) drops.add(drop.get(this, age, fortune, rand));
            return;
        }
         if (seed != null) drops.add(seed.get(this, age, fortune, rand));
    }

    @Override
    public boolean usesCustomItemHandler() {
        return !hasItem;
    }

    public BlockDaemonicaCrop setDrop(DropFunction drop) {
        this.drop = drop;
        return this;
    }

    public BlockDaemonicaCrop setSeed(DropFunction seed) {
        this.seed = seed;
        return this;
    }

    public static BlockDaemonicaCrop create(String name, int maxAge, boolean hasItem) {
        staticProp = PropertyInteger.create("age", 0, maxAge);
        return new BlockDaemonicaCrop(name, maxAge, hasItem);
    }

    public interface DropFunction {

        static DropFunction seed(ItemStack seed) {
            return (crop, age, fortune, rand) -> {
                ItemStack stack = seed.copy();
                int count = 0;
                for (int i = 0; i < fortune + 3; i++) if (rand.nextInt(2 * crop.getMaxAge()) <= age) count++;
                stack.setCount(count);
                return stack;
            };
        }

        DropFunction SELF = (crop, age, fortune, rand) -> new ItemStack(crop);

        ItemStack get(BlockDaemonicaCrop crop, int age, int fortune, Random rand);

    }
}
