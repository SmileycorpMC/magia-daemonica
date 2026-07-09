package net.smileycorp.magiadaemonica.common.capabilities;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.smileycorp.magiadaemonica.common.demons.contracts.CursesRegistry;
import net.smileycorp.magiadaemonica.common.network.PacketHandler;
import net.smileycorp.magiadaemonica.common.network.SyncCursesMessage;

import javax.annotation.Nullable;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Container class and capability for the curses system
 * Please do not use the normal capability methods other than {@link Curses#getCurses()},
 * unless you intend manually sync them to the client, as the helper methods at the bottom of this class will do that for you
 */

public interface Curses {

    @Deprecated()
    int getLevel(ResourceLocation loc);

    @Deprecated()
    int add(ResourceLocation loc);

    @Deprecated()
    void setLevel(ResourceLocation loc, int level);

    @Deprecated()
    void clear();

    @Deprecated()
    boolean remove(ResourceLocation loc);

    NBTTagList writeToNBT();

    void readFromNBT(NBTTagList nbt);

    Collection<Map.Entry<ResourceLocation, Integer>> getCurses();

    class Impl implements Curses {

        private final Map<ResourceLocation, Integer> curses = Maps.newLinkedHashMap();

        @Override
        public int getLevel(ResourceLocation loc) {
            return curses.getOrDefault(loc, 0);
        }

        @Override
        public int add(ResourceLocation loc) {
            int level = 1;
            int max = CursesRegistry.getMaxLevel(loc);
            if (max == 0) return 0;
            if (curses.containsKey(loc)) {
                level += curses.get(loc);
                if (level > max) return 0;
            }
            curses.put(loc, level);
            return level;
        }

        @Override
        public void setLevel(ResourceLocation loc, int level) {
            level = Math.min(level, CursesRegistry.getMaxLevel(loc));
            if (level == 0) {
                curses.remove(loc);
                return;
            }
            curses.put(loc, level);
        }

        @Override
        public void clear() {
            curses.clear();
        }

        @Override
        public boolean remove(ResourceLocation loc) {
            if (!curses.containsKey(loc)) return false;
            curses.remove(loc);
            return true;
        }

        @Override
        public NBTTagList writeToNBT() {
            NBTTagList nbt = new NBTTagList();
            for (Map.Entry<ResourceLocation, Integer> entry : curses.entrySet()) {
                if (entry.getValue() == 0) continue;
                NBTTagCompound compound = new NBTTagCompound();
                compound.setString("id", entry.getKey().toString());
                compound.setInteger("level", entry.getValue());
                nbt.appendTag(compound);
            }
            return nbt;
        }

        @Override
        public void readFromNBT(NBTTagList nbt) {
            for (NBTBase nbtBase : nbt) if (nbtBase instanceof NBTTagCompound) {
                NBTTagCompound compound = (NBTTagCompound) nbtBase;
                curses.put(new ResourceLocation(compound.getString("id")), compound.getInteger("level"));
            }
        }

        @Override
        public Collection<Map.Entry<ResourceLocation, Integer>> getCurses() {
            return curses.entrySet();
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            boolean first = true;
            for (Map.Entry<ResourceLocation, Integer> entry : curses.entrySet()) {
                if (!first) builder.append(", ");
                else first = false;
                builder.append(entry.getKey()).append(": ").append(entry.getValue());
            }
            return builder.toString();
        }

    }

    class Storage implements Capability.IStorage<Curses> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<Curses> capability, Curses instance, EnumFacing enumFacing) {
            return instance.writeToNBT();
        }

        @Override
        public void readNBT(Capability<Curses> capability, Curses instance, EnumFacing enumFacing, NBTBase nbtBase) {
            instance.readFromNBT((NBTTagList) nbtBase);
        }

    }

    class Provider implements ICapabilitySerializable<NBTBase> {

        protected Curses instance = DaemonicaCapabilities.CURSES.getDefaultInstance();

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == DaemonicaCapabilities.CURSES;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == DaemonicaCapabilities.CURSES ? DaemonicaCapabilities.CURSES.cast(instance) : null;
        }

        @Override
        public NBTBase serializeNBT() {
            return DaemonicaCapabilities.CURSES.getStorage().writeNBT(DaemonicaCapabilities.CURSES, instance, null);
        }

        @Override
        public void deserializeNBT(NBTBase nbt) {
            DaemonicaCapabilities.CURSES.getStorage().readNBT(DaemonicaCapabilities.CURSES, instance, null, nbt);
        }

    }

    static int getLevel(EntityPlayer player, ResourceLocation curse) {
        if (!player.hasCapability(DaemonicaCapabilities.CURSES, null)) return 0;
        return player.getCapability(DaemonicaCapabilities.CURSES, null).getLevel(curse);
    }

    static boolean has(EntityPlayer player, ResourceLocation curse) {
        return getLevel(player, curse) > 0;
    }

    static int add(EntityPlayer player, ResourceLocation curse) {
        if (!player.hasCapability(DaemonicaCapabilities.CURSES, null)) return 0;
        int level = player.getCapability(DaemonicaCapabilities.CURSES, null).add(curse);
        if (level != 0 && player instanceof EntityPlayerMP) PacketHandler.NETWORK_INSTANCE.sendTo(new SyncCursesMessage(curse, level), (EntityPlayerMP) player);
        return level;
    }

    static boolean isMaxLevel(EntityPlayer player, ResourceLocation curse) {
        if (!player.hasCapability(DaemonicaCapabilities.CURSES, null)) return true;
        return player.getCapability(DaemonicaCapabilities.CURSES, null).getLevel(curse) >= CursesRegistry.getMaxLevel(curse);
    }

    static void clear(EntityPlayer player) {
        if (!player.hasCapability(DaemonicaCapabilities.CURSES, null)) return;
        Curses curses = player.getCapability(DaemonicaCapabilities.CURSES, null);
        Collection<Map.Entry<ResourceLocation, Integer>> curseList = curses.getCurses().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), 0)).collect(Collectors.toList());
        curses.clear();
        if (player instanceof EntityPlayerMP) PacketHandler.NETWORK_INSTANCE.sendTo(new SyncCursesMessage(curseList), (EntityPlayerMP) player);
    }

    static boolean remove(EntityPlayer player, ResourceLocation curse) {
        if (!player.hasCapability(DaemonicaCapabilities.CURSES, null)) return false;
        if (!player.getCapability(DaemonicaCapabilities.CURSES, null).remove(curse)) return false;
        PacketHandler.NETWORK_INSTANCE.sendTo(new SyncCursesMessage(curse, 0), (EntityPlayerMP) player);
        return true;
    }

}
