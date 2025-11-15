package net.smileycorp.magiadaemonica.common.blocks.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.common.blocks.BlockChalkLine;
import net.smileycorp.magiadaemonica.common.blocks.DaemonicaBlocks;

public class TileSummoningCircle extends TileEntity {

    private BlockPos origin = BlockPos.ORIGIN;
    private int width = 0, height = 0;
    private ResourceLocation circle;
    private EnumFacing facing;
    private boolean mirror;

    public void setOrigin(BlockPos origin, int width, int height) {
        this.origin = origin;
        this.width = width;
        this.height = height;
        markDirty();
    }

    public void setRenderProperties(ResourceLocation circle, EnumFacing facing, boolean mirror) {
        this.circle = circle;
        this.facing = facing;
        this.mirror = mirror;
        markDirty();
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState state, IBlockState newState) {
        if (newState.getBlock() != DaemonicaBlocks.CHALK_LINE) return false;
        return newState.getValue(BlockChalkLine.ACTIVE);
    }

    public void breakRitual(BlockPos pos) {
        BlockPos.MutableBlockPos mutable;
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < height; z++) {
                mutable = new BlockPos.MutableBlockPos(origin.getX() + x, origin.getY(), origin.getZ() + z);
                if (mutable.equals(pos)) continue;
                world.removeTileEntity(mutable);
                IBlockState state = world.getBlockState(mutable);
                if (state.getBlock() != DaemonicaBlocks.CHALK_LINE) continue;
                world.setBlockState(mutable, state.withProperty(BlockChalkLine.ACTIVE, false));
            }
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("origin")) origin = NBTUtil.getPosFromTag(nbt.getCompoundTag("origin"));
        if (nbt.hasKey("width")) width = nbt.getInteger("width");
        if (nbt.hasKey("height")) height = nbt.getInteger("height");
        if (nbt.hasKey("circle")) circle = new ResourceLocation(nbt.getString("circle"));
        if (nbt.hasKey("facing")) facing = EnumFacing.values()[nbt.getByte("facing")];
        if (nbt.hasKey("mirror")) mirror = nbt.getBoolean("mirror");
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        nbt.setTag("origin", NBTUtil.createPosTag(origin));
        nbt.setInteger("width", width);
        nbt.setInteger("height", height);
        if (circle != null) {
            nbt.setString("circle", circle.toString());
            nbt.setByte("facing", (byte) facing.ordinal());
            nbt.setBoolean("mirror", mirror);
        }
        return super.writeToNBT(nbt);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = super.getUpdateTag();
        nbt.setTag("origin", NBTUtil.createPosTag(origin));
        nbt.setInteger("width", width);
        nbt.setInteger("height", height);
        if (circle != null) {
            nbt.setString("circle", circle.toString());
            nbt.setByte("facing", (byte) facing.ordinal());
            nbt.setBoolean("mirror", mirror);
        }
        return nbt;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("origin")) origin = NBTUtil.getPosFromTag(nbt.getCompoundTag("origin"));
        if (nbt.hasKey("width")) width = nbt.getInteger("width");
        if (nbt.hasKey("height")) height = nbt.getInteger("height");
        if (nbt.hasKey("circle")) circle = new ResourceLocation(nbt.getString("circle"));
        if (nbt.hasKey("facing")) facing = EnumFacing.values()[nbt.getByte("facing")];
        if (nbt.hasKey("mirror")) mirror = nbt.getBoolean("mirror");
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    public BlockPos getOrigin() {
        return origin;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public ResourceLocation getCircle() {
        return circle;
    }

    public EnumFacing getFacing() {
        return facing;
    }

}
