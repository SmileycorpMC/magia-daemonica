package net.smileycorp.magiadaemonica.common.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.DaemonicaSoundEvents;
import net.smileycorp.magiadaemonica.common.MagiaDaemonica;
import net.smileycorp.magiadaemonica.common.blocks.tiles.TileScrollshelf;
import net.smileycorp.magiadaemonica.config.BlocksConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BlockScrollshelf extends BlockHorizontal implements BlockProperties, ITileEntityProvider {
    
    public static final PropertyBool[] SCROLLS = {PropertyBool.create("scroll_0"), PropertyBool.create("scroll_1"),
    PropertyBool.create("scroll_2"), PropertyBool.create("scroll_3"), PropertyBool.create("scroll_4"),
    PropertyBool.create("scroll_5"), PropertyBool.create("scroll_6"), PropertyBool.create("scroll_7")};

    public BlockScrollshelf() {
        super(Material.WOOD);
        setUnlocalizedName(Constants.name("scrollshelf"));
        setRegistryName(Constants.loc("scrollshelf"));
        setCreativeTab(MagiaDaemonica.CREATIVE_TAB);
        setSoundType(SoundType.WOOD);
        setHardness(BlocksConfig.scrollshelf.getHardness());
        setResistance(BlocksConfig.scrollshelf.getResistance());
        setHarvestLevel("pickaxe", BlocksConfig.scrollshelf.getHarvestLevel());
        IBlockState state = blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH);
        for (PropertyBool scroll : SCROLLS) state = state.withProperty(scroll, false);
        setDefaultState(state);
    }

    @Override
    protected @NotNull BlockStateContainer createBlockState() {
        IProperty<?>[] properties = new IProperty[SCROLLS.length + 1];
        properties[0] = FACING;
        System.arraycopy(SCROLLS, 0, properties, 1, SCROLLS.length);
        return new BlockStateContainer(this, properties);
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(FACING, EnumFacing.values()[meta % 4 + 2]);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(FACING).getIndex() - 2;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);
        if (!(tile instanceof TileScrollshelf)) return state;
        TileScrollshelf scrollshelf = (TileScrollshelf) tile;
        for (int i = 0; i < SCROLLS.length; i++) if (scrollshelf.hasScroll(i)) state = state.withProperty(SCROLLS[i], true);
        return state;
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

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileScrollshelf();
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileScrollshelf) ((TileScrollshelf)te).breakBlock(world, pos);
        super.breakBlock(world, pos, state);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        EnumFacing facing1 = state.getValue(FACING);
        if (facing1 != facing) return false;
        TileEntity te = world.getTileEntity(pos);
        if (!(te instanceof TileScrollshelf)) return false;
        TileScrollshelf scrollshelf = (TileScrollshelf) te;
        int slot = getHitSlot(facing, hitX, hitY, hitZ);
        ItemStack stack = player.getHeldItem(hand);
        boolean hasScroll = scrollshelf.hasScroll(slot);
        if (hasScroll && stack.isEmpty()) {
            if (world.isRemote) return true;
            ItemStack newStack = scrollshelf.getStackInSlot(slot);
            scrollshelf.setInventorySlotContents(slot, ItemStack.EMPTY);
            player.setHeldItem(hand, newStack);
            world.playSound(null, pos, DaemonicaSoundEvents.PAGE_TURN, SoundCategory.HOSTILE, 0.75f, 1);
            return true;
        }
        if (!hasScroll && ((TileScrollshelf) te).isItemValidForSlot(slot, stack)) {
            if (world.isRemote) return true;
            scrollshelf.setInventorySlotContents(slot, stack);
            if (!player.isCreative()) stack.shrink(1);
            world.playSound(null, pos, DaemonicaSoundEvents.PAGE_TURN, SoundCategory.HOSTILE, 0.75f, 0.75f);
            return true;
        }
        return false;
    }

    @Deprecated
    public boolean hasComparatorInputOverride(IBlockState state) {
        return true;
    }

    @Deprecated
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
        TileEntity te = world.getTileEntity(pos);
        return te instanceof TileScrollshelf ? ((TileScrollshelf) te).getComparatorLevel() : 0;
    }

    //work out the slot the player is clicking on
    private int getHitSlot(EnumFacing facing, float hitX, float hitY, float hitZ) {
        //we want to rotate the hit vectors to north, which instead of using the sin/cos functions
        //we can approximate as sin(90) = 1, sin(180) = 0, sin(270) = -1, ect..
        //cosine is just sin + 90
        Vec3i vec = facing.getDirectionVec();
        float sin = vec.getX();
        float cos = vec.getZ();
        //we don't need z of hit detection, so we only use it for rotation
        //change the x and y to be on a grid from (-1, -1) to (1, 1) instead of (0, 0) to (1, 1) to simplify math later
        float x = 2f * (hitX - 0.5f) * cos - 2f * (hitZ - 0.5f)  * sin;
        float y = 2f * (hitY - 0.5f);
        //is the point (x, y) to the top left of the line {(-1, 0) (0, 1)}?
        if (x + 1 - y <= 0) return 0;
        //is the point (x, y) to the top right of the line {(0, 1) (1, 0)}?
        if (x + y - 1 >= 0) return 2;
        //is the point (x, y) to the bottom left of the line {(-1, 0) (0, -1)}?
        if (x + y + 1 <= 0) return 5;
        //is the point (x, y) to the bottom right of the line {(0, -1) (1, 0)}?
        if (1 - x + y <= 0) return 7;
        //is the point (x, y) to the top left of the line {(-1, -1) (1, 1)}?
        boolean topLeft = (x + 1) * (2) - (y + 1) * (2) <= 0;
        //is the point (x, y) to the top right of the line {(-1, 1) (1, -1)}?
        boolean topRight = (x + 1) * -2 - (y - 1) * 2 <= 0;
        if (topLeft) return topRight ? 1 : 3;
        return topRight ? 4 : 6;
    }

}
