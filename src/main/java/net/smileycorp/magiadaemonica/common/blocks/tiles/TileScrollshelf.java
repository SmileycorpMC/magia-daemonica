package net.smileycorp.magiadaemonica.common.blocks.tiles;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.config.BlocksConfig;

import javax.annotation.Nullable;

public class TileScrollshelf extends TileEntity implements IInventory {

    private final NonNullList<ItemStack> items = NonNullList.withSize(8, ItemStack.EMPTY);

    @Override
    public int getSizeInventory() {
        return items.size();
    }

    @Override
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot < 0 || slot >= items.size() ? ItemStack.EMPTY : items.get(slot);
    }

    @Override
    public ItemStack decrStackSize(int slot, int count) {
        return removeStackFromSlot(slot);
    }

    @Override
    public ItemStack removeStackFromSlot(int slot) {
        ItemStack stack = items.set(slot, ItemStack.EMPTY);
        markDirty();
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        if (slot < 0 || slot >= items.size()) return;
        if (!stack.isEmpty()) {
            stack = stack.copy();
            stack.setCount(1);
        }
        items.set(slot, stack);
        markDirty();
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    public boolean hasScroll(int slot) {
        return slot >= 0 && slot < items.size() && !items.get(slot).isEmpty();
    }

    @Override
    public void markDirty() {
        super.markDirty();
        IBlockState state = world.getBlockState(pos);
        world.markBlockRangeForRenderUpdate(pos, pos);
        world.notifyBlockUpdate(pos, state, state, 3);
        world.scheduleBlockUpdate(pos, getBlockType(), 0, 0);
    }

    @Override
    public boolean isUsableByPlayer(EntityPlayer player) {
        return false;
    }

    @Override
    public void openInventory(EntityPlayer player) {}

    @Override
    public void closeInventory(EntityPlayer player) {}

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return slot >= 0 && slot < items.size() && BlocksConfig.canPlaceInScrollshelf(stack);
    }

    @Override
    public int getField(int id) {
        return 0;
    }

    @Override
    public void setField(int id, int value) {}

    @Override
    public int getFieldCount() {
        return 0;
    }

    @Override
    public void clear() {
        items.clear();
        markDirty();
    }

    @Override
    public void setWorld(World world) {
        super.setWorld(world);
        world.markBlockRangeForRenderUpdate(pos, pos);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        ItemStackHelper.loadAllItems(nbt, items);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        ItemStackHelper.saveAllItems(nbt, items);
        return super.writeToNBT(nbt);
    }

    @Override
    public NBTTagCompound getUpdateTag() {
        NBTTagCompound nbt = super.getUpdateTag();
        ItemStackHelper.saveAllItems(nbt, items);
        return nbt;
    }

    @Override
    public void handleUpdateTag(NBTTagCompound nbt) {
        super.handleUpdateTag(nbt);
        items.clear();
        ItemStackHelper.loadAllItems(nbt, items);
    }

    @Override
    @Nullable
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        super.onDataPacket(net, pkt);
        handleUpdateTag(pkt.getNbtCompound());
        world.markBlockRangeForRenderUpdate(pos, pos);
    }

    public int getComparatorLevel() {
        return (int) items.stream().filter(stack -> !stack.isEmpty()).count() * 15 / getSizeInventory();
    }

    public void breakBlock(World world, BlockPos pos) {
        for (ItemStack stack : items) {
            if (stack.isEmpty()) continue;
            Block.spawnAsEntity(world, pos, stack);
        }
    }

    @Override
    public String getName() {
        return "tile.magiadaemonica.scrollshelf";
    }

    @Override
    public boolean hasCustomName() {
        return false;
    }

}
