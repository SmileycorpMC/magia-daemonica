package net.smileycorp.magiadaemonica.common.capabilities;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.smileycorp.atlas.api.data.Pair;

import javax.annotation.Nullable;
import java.util.List;

public interface ComponentTracker {

    void addDamage(float damage);

    float getTakenDamage();

    void addFood(ItemStack stack);

    boolean hasEaten(ItemStack stack);

    class Impl implements ComponentTracker {

        private final EntityPlayer player;

        public Impl(EntityPlayer player) {
            this.player = player;
        }

        //damage
        private final List<Pair<Float, Long>> takenDamage = Lists.newArrayList();

        @Override
        public void addDamage(float damage) {
            for (int i = 0; i < takenDamage.size(); i++) {
                Pair<Float, Long> pair = takenDamage.get(i);
                if (pair.getSecond() + 600 <= player.world.getWorldTime()) continue;
                if (i > 0) takenDamage.subList(0, i).clear();
                break;
            }
            takenDamage.add(Pair.of(damage, player.world.getWorldTime()));
        }

        @Override
        public float getTakenDamage() {
            if (takenDamage.isEmpty()) return 0;
            int remove = -1;
            float damage = 0;
            for (int i = 0; i < takenDamage.size(); i++) {
                Pair<Float, Long> pair = takenDamage.get(i);
                if (pair.getSecond() + 600 <= player.world.getWorldTime()) continue;
                if (remove == -1) remove = i;
                damage += pair.getFirst();
            }
            if (remove > 0) takenDamage.subList(0, remove).clear();
            return damage;
        }

        //food
        private final List<Pair<ItemStack, Long>> eatenFood = Lists.newArrayList();

        @Override
        public void addFood(ItemStack stack) {
            for (int i = 0; i < eatenFood.size(); i++) {
                Pair<ItemStack, Long> pair = eatenFood.get(i);
                if (pair.getSecond() + 600 <= player.world.getWorldTime()) continue;
                if (i > 0) eatenFood.subList(0, i).clear();
                break;
            }
            eatenFood.add(Pair.of(stack, player.world.getWorldTime()));
        }

        @Override
        public boolean hasEaten(ItemStack stack) {
            if (eatenFood.isEmpty()) return false;
            int remove = -1;
            for (int i = 0; i < eatenFood.size(); i++) {
                Pair<ItemStack, Long> pair = eatenFood.get(i);
                if (pair.getSecond() + 600 <= player.world.getWorldTime()) continue;
                if (remove == -1) remove = i;
                if (ItemStack.areItemsEqual(stack, pair.getFirst())) {
                    if (remove > 0) eatenFood.subList(0, remove).clear();
                    return true;
                }
            }
            if (remove > 0) eatenFood.subList(0, remove).clear();
            return false;
        }

    }

    class Storage implements Capability.IStorage<ComponentTracker> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<ComponentTracker> capability, ComponentTracker sanguis, EnumFacing enumFacing) {
            return null;
        }

        @Override
        public void readNBT(Capability<ComponentTracker> capability, ComponentTracker sanguis, EnumFacing enumFacing, NBTBase nbtBase) {}

    }

    class Provider implements ICapabilityProvider {

        protected final ComponentTracker instance;

        public Provider(EntityPlayer player) {
            instance = new Impl(player);
        }

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == DaemonicaCapabilities.COMPONENT_TRACKER;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == DaemonicaCapabilities.COMPONENT_TRACKER ? DaemonicaCapabilities.COMPONENT_TRACKER.cast(instance) : null;
        }

    }

    static float getDamage(EntityPlayer player) {
        return player.hasCapability(DaemonicaCapabilities.COMPONENT_TRACKER, null) ?
                player.getCapability(DaemonicaCapabilities.COMPONENT_TRACKER, null).getTakenDamage() : 0;
    }

    static void addDamage(EntityPlayer player, float damage) {
        if (player.hasCapability(DaemonicaCapabilities.COMPONENT_TRACKER, null))
                player.getCapability(DaemonicaCapabilities.COMPONENT_TRACKER, null).addDamage(damage);
    }

    static boolean hasEaten(EntityPlayer player, ItemStack stack) {
        return player.hasCapability(DaemonicaCapabilities.COMPONENT_TRACKER, null) &&
                player.getCapability(DaemonicaCapabilities.COMPONENT_TRACKER, null).hasEaten(stack);
    }

    static void addFood(EntityPlayer player, ItemStack stack) {
        if (player.hasCapability(DaemonicaCapabilities.COMPONENT_TRACKER, null))
            player.getCapability(DaemonicaCapabilities.COMPONENT_TRACKER, null).addFood(stack);
    }

}
