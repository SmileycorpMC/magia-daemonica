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
import net.smileycorp.magiadaemonica.common.demons.contracts.BoonRegistry;
import net.smileycorp.magiadaemonica.common.network.PacketHandler;
import net.smileycorp.magiadaemonica.common.network.SyncBoonsMessage;

import javax.annotation.Nullable;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Container class and capability for the boons system
 * Please do not use the normal capability methods other than {@link Boons#getBoons()},
 * unless you intend manually sync them to the client, as the helper methods at the bottom of this class will do that for you
 */

public interface Boons {

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

    Collection<Map.Entry<ResourceLocation, Integer>> getBoons();

    class Impl implements Boons {

        private final Map<ResourceLocation, Integer> boons = Maps.newLinkedHashMap();

        @Override
        public int getLevel(ResourceLocation loc) {
            return boons.getOrDefault(loc, 0);
        }

        @Override
        public int add(ResourceLocation loc) {
            int level = 1;
            int max = BoonRegistry.getMaxLevel(loc);
            if (max == 0) return 0;
            if (boons.containsKey(loc)) {
                level += boons.get(loc);
                if (level > max) return 0;
            }
            boons.put(loc, level);
            return level;
        }

        @Override
        public void setLevel(ResourceLocation loc, int level) {
            level = Math.min(level, BoonRegistry.getMaxLevel(loc));
            if (level == 0) {
                boons.remove(loc);
                return;
            }
            boons.put(loc, level);
        }

        @Override
        public void clear() {
            boons.clear();
        }

        @Override
        public boolean remove(ResourceLocation loc) {
            if (!boons.containsKey(loc)) return false;
            boons.remove(loc);
            return true;
        }

        @Override
        public NBTTagList writeToNBT() {
            NBTTagList nbt = new NBTTagList();
            for (Map.Entry<ResourceLocation, Integer> entry : boons.entrySet()) {
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
                boons.put(new ResourceLocation(compound.getString("id")), compound.getInteger("level"));
            }
        }

        @Override
        public Collection<Map.Entry<ResourceLocation, Integer>> getBoons() {
            return boons.entrySet();
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            boolean first = true;
            for (Map.Entry<ResourceLocation, Integer> entry : boons.entrySet()) {
                if (!first) builder.append(", ");
                else first = false;
                builder.append(entry.getKey()).append(": ").append(entry.getValue());
            }
            return builder.toString();
        }

    }

    class Storage implements Capability.IStorage<Boons> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<Boons> capability, Boons instance, EnumFacing enumFacing) {
            return instance.writeToNBT();
        }

        @Override
        public void readNBT(Capability<Boons> capability, Boons instance, EnumFacing enumFacing, NBTBase nbtBase) {
            instance.readFromNBT((NBTTagList) nbtBase);
        }

    }

    class Provider implements ICapabilitySerializable<NBTBase> {

        protected Boons instance = DaemonicaCapabilities.BOONS.getDefaultInstance();

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == DaemonicaCapabilities.BOONS;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == DaemonicaCapabilities.BOONS ? DaemonicaCapabilities.BOONS.cast(instance) : null;
        }

        @Override
        public NBTBase serializeNBT() {
            return DaemonicaCapabilities.BOONS.getStorage().writeNBT(DaemonicaCapabilities.BOONS, instance, null);
        }

        @Override
        public void deserializeNBT(NBTBase nbt) {
            DaemonicaCapabilities.BOONS.getStorage().readNBT(DaemonicaCapabilities.BOONS, instance, null, nbt);
        }

    }

    static int getLevel(EntityPlayer player, ResourceLocation boon) {
        if (!player.hasCapability(DaemonicaCapabilities.BOONS, null)) return 0;
        return player.getCapability(DaemonicaCapabilities.BOONS, null).getLevel(boon);
    }

    static boolean has(EntityPlayer player, ResourceLocation boon) {
        return getLevel(player, boon) > 0;
    }

    static int add(EntityPlayer player, ResourceLocation boon) {
        if (!player.hasCapability(DaemonicaCapabilities.BOONS, null)) return 0;
        int level = player.getCapability(DaemonicaCapabilities.BOONS, null).add(boon);
        if (level != 0 && player instanceof EntityPlayerMP) PacketHandler.NETWORK_INSTANCE.sendTo(new SyncBoonsMessage(boon, level), (EntityPlayerMP) player);
        return level;
    }

    static boolean isMaxLevel(EntityPlayer player, ResourceLocation boon) {
        if (!player.hasCapability(DaemonicaCapabilities.BOONS, null)) return true;
        return player.getCapability(DaemonicaCapabilities.BOONS, null).getLevel(boon) >= BoonRegistry.getMaxLevel(boon);
    }

    static void clear(EntityPlayer player) {
        if (!player.hasCapability(DaemonicaCapabilities.BOONS, null)) return;
        Boons boons = player.getCapability(DaemonicaCapabilities.BOONS, null);
        Collection<Map.Entry<ResourceLocation, Integer>> boonList = boons.getBoons().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), 0)).collect(Collectors.toList());
        boons.clear();
        if (player instanceof EntityPlayerMP) PacketHandler.NETWORK_INSTANCE.sendTo(new SyncBoonsMessage(boonList), (EntityPlayerMP) player);
    }

    static boolean remove(EntityPlayer player, ResourceLocation boon) {
        if (!player.hasCapability(DaemonicaCapabilities.BOONS, null)) return false;
        if (!player.getCapability(DaemonicaCapabilities.BOONS, null).remove(boon)) return false;
        PacketHandler.NETWORK_INSTANCE.sendTo(new SyncBoonsMessage(boon, 0), (EntityPlayerMP) player);
        return true;
    }

}
