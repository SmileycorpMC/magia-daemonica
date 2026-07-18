package net.smileycorp.magiadaemonica.common.capabilities;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import javax.annotation.Nullable;

public interface Sanguis {

    void addDamage(float damage);

    float getDamage();

    class Impl implements Sanguis {

        private final EntityPlayer player;
        private float damage = 0;
        private long timeStamp = -1;

        public Impl(EntityPlayer player) {
            this.player = player;
        }

        @Override
        public void addDamage(float damage) {
            checkClear();
            this.damage += damage;
            timeStamp = player.world.getWorldTime();
        }

        @Override
        public float getDamage() {
            checkClear();
            return damage;
        }

        private void checkClear() {
            if (timeStamp + 600 > player.world.getWorldTime()) return;
            damage = 0;
            timeStamp = -1;
        }

    }

    class Storage implements Capability.IStorage<Sanguis> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<Sanguis> capability, Sanguis sanguis, EnumFacing enumFacing) {
            return null;
        }

        @Override
        public void readNBT(Capability<Sanguis> capability, Sanguis sanguis, EnumFacing enumFacing, NBTBase nbtBase) {}

    }

    class Provider implements ICapabilityProvider {

        protected final Sanguis instance;

        public Provider(EntityPlayer player) {
            instance = new Impl(player);
        }

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == DaemonicaCapabilities.SANGUIS;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == DaemonicaCapabilities.SANGUIS ? DaemonicaCapabilities.SANGUIS.cast(instance) : null;
        }

    }

    static float get(EntityPlayer player) {
        return player.hasCapability(DaemonicaCapabilities.SANGUIS, null) ?
                player.getCapability(DaemonicaCapabilities.SANGUIS, null).getDamage() : 0;
    }

    static void add(EntityPlayer player, float damage) {
        if (player.hasCapability(DaemonicaCapabilities.SANGUIS, null))
                player.getCapability(DaemonicaCapabilities.SANGUIS, null).addDamage(damage);
    }

}
