package net.smileycorp.magiadaemonica.common.capabilities;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;

public interface ISoul {

    float getSoul();

    void consumeSoul(float percent, boolean flat);

    void setSoul(float percent);

    class Soul implements ISoul {

        private float soul_percent = 1;

        @Override
        public float getSoul() {
            return soul_percent;
        }

        @Override
        public void consumeSoul(float percent, boolean flat) {
            setSoul(Math.max(0, flat ? soul_percent - percent : soul_percent * (1 - percent)));
        }

        @Override
        public void setSoul(float percent) {
            soul_percent = percent;
        }

    }

    class Storage implements Capability.IStorage<ISoul> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<ISoul> capability, ISoul soul, EnumFacing enumFacing) {
            return new NBTTagFloat(soul.getSoul());
        }

        @Override
        public void readNBT(Capability<ISoul> capability, ISoul soul, EnumFacing enumFacing, NBTBase nbtBase) {
            soul.setSoul(((NBTTagFloat)nbtBase).getFloat());
        }

    }

    class Provider implements ICapabilitySerializable<NBTBase> {

        protected ISoul instance = MagiaDaemonicaCapabilities.SOUL.getDefaultInstance();

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == MagiaDaemonicaCapabilities.SOUL;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == MagiaDaemonicaCapabilities.SOUL ? MagiaDaemonicaCapabilities.SOUL.cast(instance) : null;
        }

        @Override
        public NBTBase serializeNBT() {
            return MagiaDaemonicaCapabilities.SOUL.getStorage().writeNBT(MagiaDaemonicaCapabilities.SOUL, instance, null);
        }

        @Override
        public void deserializeNBT(NBTBase nbt) {
            MagiaDaemonicaCapabilities.SOUL.getStorage().readNBT(MagiaDaemonicaCapabilities.SOUL, instance, null, nbt);
        }

    }

}
