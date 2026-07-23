package net.smileycorp.magiadaemonica.common.capabilities;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.smileycorp.magiadaemonica.common.network.PacketHandler;
import net.smileycorp.magiadaemonica.common.network.SyncEffectsMessage;
import net.smileycorp.magiadaemonica.common.util.Effect;

import javax.annotation.Nullable;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public interface Effects {

    void tick(EntityPlayerMP player);

    @Deprecated()
    Effect get(ResourceLocation loc);

    @Deprecated()
    void addEffect(ResourceLocation loc, Effect effect);

    @Deprecated()
    void clear();

    @Deprecated()
    boolean remove(ResourceLocation loc);

    Collection<Map.Entry<ResourceLocation, Effect>> getEffects();

    class Impl implements Effects {

        private final Map<ResourceLocation, Effect> effects = Maps.newLinkedHashMap();

        @Override
        public void tick(EntityPlayerMP player) {
            for (Map.Entry<ResourceLocation, Effect> entry : effects.entrySet()) {
                Effect effect = entry.getValue();
                effect.setDuration(effect.getDuration() - 1);
                if (effect.getDuration() < 0) {
                    PacketHandler.NETWORK_INSTANCE.sendTo(new SyncEffectsMessage(entry.getKey(), new Effect(0, 0)), player);
                    remove(entry.getKey());
                }
            }
        }

        @Override
        public Effect get(ResourceLocation loc) {
            return effects.getOrDefault(loc, null);
        }

        @Override
        public void addEffect(ResourceLocation loc, Effect effect) {
            if (effect.getLevel() == 0 || effect.getDuration() == 0) {
                effects.remove(loc);
                return;
            }
            effects.put(loc, effect);
        }

        @Override
        public void clear() {
            effects.clear();
        }

        @Override
        public boolean remove(ResourceLocation loc) {
            if (!effects.containsKey(loc)) return false;
            effects.remove(loc);
            return true;
        }

        @Override
        public Collection<Map.Entry<ResourceLocation, Effect>> getEffects() {
            return effects.entrySet();
        }
    }

    class Storage implements Capability.IStorage<Effects> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<Effects> capability, Effects effects, EnumFacing enumFacing) {
            return null;
        }

        @Override
        public void readNBT(Capability<Effects> capability, Effects effects, EnumFacing enumFacing, NBTBase nbtBase) {}

    }

    class Provider implements ICapabilityProvider {

        protected Effects instance = DaemonicaCapabilities.EFFECTS.getDefaultInstance();

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == DaemonicaCapabilities.EFFECTS;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == DaemonicaCapabilities.EFFECTS ? DaemonicaCapabilities.EFFECTS.cast(instance) : null;
        }

    }

    static Effect get(EntityPlayer player, ResourceLocation effect) {
        if (!player.hasCapability(DaemonicaCapabilities.EFFECTS, null)) return null;
        return player.getCapability(DaemonicaCapabilities.EFFECTS, null).get(effect);
    }

    static boolean has(EntityPlayer player, ResourceLocation effect) {
        return get(player, effect) != null;
    }

    static void addEffect(EntityPlayer player, ResourceLocation name, Effect effect) {
        if (!player.hasCapability(DaemonicaCapabilities.EFFECTS, null)) return;
        player.getCapability(DaemonicaCapabilities.EFFECTS, null).addEffect(name, effect);
        if (player instanceof EntityPlayerMP) PacketHandler.NETWORK_INSTANCE.sendTo(new SyncEffectsMessage(name, effect), (EntityPlayerMP) player);
    }

    static void clear(EntityPlayer player) {
        if (!player.hasCapability(DaemonicaCapabilities.EFFECTS, null)) return;
        Effects effects = player.getCapability(DaemonicaCapabilities.EFFECTS, null);
        Collection<Map.Entry<ResourceLocation, Effect>> effectList = effects.getEffects().stream()
                .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), new Effect(0, 0))).collect(Collectors.toList());
        effects.clear();
        if (player instanceof EntityPlayerMP) PacketHandler.NETWORK_INSTANCE.sendTo(new SyncEffectsMessage(effectList), (EntityPlayerMP) player);
    }

    static boolean remove(EntityPlayer player, ResourceLocation effect) {
        if (!player.hasCapability(DaemonicaCapabilities.EFFECTS, null)) return false;
        if (!player.getCapability(DaemonicaCapabilities.EFFECTS, null).remove(effect)) return false;
        PacketHandler.NETWORK_INSTANCE.sendTo(new SyncEffectsMessage(effect, new Effect(0, 0)), (EntityPlayerMP) player);
        return true;
    }

}
