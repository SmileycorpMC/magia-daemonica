package net.smileycorp.magiadaemonica.common.blocks;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.SoundType;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.smileycorp.magiadaemonica.common.items.DaemonicaItems;
import net.smileycorp.magiadaemonica.common.rituals.RitualsServer;
import net.smileycorp.magiadaemonica.common.rituals.summoning.SummoningCircles;

import java.util.Random;

public class BlockChalkLine extends BlockLine implements Lightable {

    public static final PropertyEnum<Candle> CANDLE = PropertyEnum.create("candle", Candle.class);
    public static final PropertyEnum<RitualState> RITUAL_STATE = PropertyEnum.create("ritual_state", RitualState.class);

    public BlockChalkLine() {
        super("chalk_line");
        setDefaultState(blockState.getBaseState().withProperty(NORTH, false).withProperty(EAST, false).withProperty(SOUTH, false)
                .withProperty(WEST, false).withProperty(CANDLE, Candle.NONE).withProperty(RITUAL_STATE, RitualState.NONE));
    }

    @Override
    public BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, NORTH, EAST, SOUTH, WEST, CANDLE, RITUAL_STATE);
    }

    @Override
    public int quantityDropped(Random random) {
        return 0;
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult raytrace, World world, BlockPos pos, EntityPlayer player) {
        return state.getValue(CANDLE) == Candle.NONE ? new ItemStack(DaemonicaItems.CHALK_STICK) :
                new ItemStack(DaemonicaBlocks.SCENTED_CANDLE);
    }

    @Override
    public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
        if (state.getValue(CANDLE) == Candle.NONE) return super.removedByPlayer(state, world, pos, player, willHarvest);
        world.setBlockState(pos, state.withProperty(CANDLE, Candle.NONE));
        if (world.isRemote) return false;
        if (willHarvest) {
            EntityItem entityitem = new EntityItem(world, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f,
                    new ItemStack(DaemonicaBlocks.SCENTED_CANDLE));
            entityitem.setDefaultPickupDelay();
            world.spawnEntity(entityitem);
        }
        if (state.getValue(RITUAL_STATE) != RitualState.NONE) RitualsServer.get((WorldServer) world).removeRitual(pos);
        return false;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        if (world.isRemote) return;
        if (state.getValue(RITUAL_STATE) != RitualState.NONE) RitualsServer.get((WorldServer) world).removeRitual(pos);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        ItemStack stack = player.getHeldItem(hand);
        if (player.isSneaking()) return false;
        if (state.getValue(RITUAL_STATE) != RitualState.NONE) return false;
        if (state.getValue(CANDLE) != Candle.NONE) return false;
        if (stack.getItem() != Item.getItemFromBlock(DaemonicaBlocks.SCENTED_CANDLE)) return false;
        if (!world.isRemote) {
            world.setBlockState(pos, state.withProperty(CANDLE, Candle.UNLIT));
            if (player instanceof EntityPlayerMP) CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
            SoundType sound = DaemonicaBlocks.SCENTED_CANDLE.getSoundType();
            world.playSound(null, pos.getX() + 0.5f, pos.getY() + 0.5f, pos.getZ() + 0.5f, sound.getPlaceSound(),
                    SoundCategory.BLOCKS, (sound.getVolume() + 1f) / 2f, sound.getPitch() * 0.8F);
            player.swingArm(hand);
            if (!player.isCreative()) stack.shrink(1);
            SummoningCircles.tryPlace(world, pos);
        }
        return true;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(CANDLE) == Candle.NONE ? AABB : FULL_BLOCK_AABB;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return getDefaultState().withProperty(CANDLE, Candle.get(meta)).withProperty(RITUAL_STATE, RitualState.get(meta));
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(RITUAL_STATE).ordinal() * RitualState.values().length + state.getValue(CANDLE).ordinal();
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
        return state.getValue(CANDLE) == Candle.LIT ? 4 : 0;
    }

    @Override
    public EnumBlockRenderType getRenderType(IBlockState state) {
        return (state.getValue(RITUAL_STATE) != RitualState.NONE) ? EnumBlockRenderType.INVISIBLE : super.getRenderType(state);
    }

    @Override
    public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
        if (state.getValue(CANDLE) != Candle.LIT || state.getValue(RITUAL_STATE) != RitualState.NONE) return;
        world.spawnParticle(EnumParticleTypes.FLAME, pos.getX() + 0.5, pos.getY() + 0.6, pos.getZ() + 0.5, 0, 0, 0);
        if (rand.nextInt(4) == 0) world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, pos.getX() + 0.5 + (rand.nextFloat() - 0.5) * 0.05,
                pos.getY() + 0.6, pos.getZ() + 0.5 + (rand.nextFloat() - 0.5) * 0.05, 0, 0, 0);
    }

    @Override
    public boolean isLightable(World world, BlockPos pos, IBlockState state) {
        return state.getValue(CANDLE) == Candle.UNLIT && state.getValue(RITUAL_STATE) != RitualState.ACTIVE;
    }

    @Override
    public void light(World world, BlockPos pos, IBlockState state) {
        world.setBlockState(pos, state.withProperty(CANDLE, Candle.LIT));
    }

    public enum Candle implements IStringSerializable {

        NONE("none"),
        UNLIT("unlit"),
        LIT("lit");

        private final String name;

        Candle(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        public static Candle get(int meta) {
            return values()[meta % values().length];
        }

    }

    public enum RitualState implements IStringSerializable {

        NONE("none"),
        INACTIVE("inactive"),
        ACTIVE("active");

        private final String name;

        RitualState(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }

        public static RitualState get(int meta) {
            return values()[(meta / values().length) % values().length];
        }

    }

}
