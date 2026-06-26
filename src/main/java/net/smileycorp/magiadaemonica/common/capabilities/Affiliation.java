package net.smileycorp.magiadaemonica.common.capabilities;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.smileycorp.magiadaemonica.common.demons.Domain;

import javax.annotation.Nullable;
import java.util.EnumMap;

public interface Affiliation {

    void setAffiliation(Domain domain, int value);

    int getAffiliation(Domain domain);

    class Impl implements Affiliation {

        private final EnumMap<Domain, Integer> affiliation;

        public Impl() {
            affiliation = Maps.newEnumMap(Domain.class);
            for (Domain domain : Domain.values()) affiliation.put(domain, 0);
        }

        @Override
        public void setAffiliation(Domain domain, int value) {
            affiliation.put(domain, MathHelper.clamp(value, -100, 100));
        }

        @Override
        public int getAffiliation(Domain domain) {
            return affiliation.get(domain);
        }

    }

    class Storage implements Capability.IStorage<Affiliation> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<Affiliation> capability, Affiliation affiliation, EnumFacing enumFacing) {
            NBTTagCompound nbt = new NBTTagCompound();
            for (Domain domain : Domain.values()) nbt.setInteger(domain.getName(), affiliation.getAffiliation(domain));
            return nbt;
        }

        @Override
        public void readNBT(Capability<Affiliation> capability, Affiliation affiliation, EnumFacing enumFacing, NBTBase nbtBase) {
            NBTTagCompound nbt = (NBTTagCompound) nbtBase;
            for (Domain domain : Domain.values()) if (nbt.hasKey(domain.getName()))
                affiliation.setAffiliation(domain, nbt.getInteger(domain.getName()));
        }

    }

    class Provider implements ICapabilitySerializable<NBTBase> {

        protected Affiliation instance = DaemonicaCapabilities.AFFILIATION.getDefaultInstance();

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == DaemonicaCapabilities.AFFILIATION;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == DaemonicaCapabilities.AFFILIATION ? DaemonicaCapabilities.AFFILIATION.cast(instance) : null;
        }

        @Override
        public NBTBase serializeNBT() {
            return DaemonicaCapabilities.AFFILIATION.getStorage().writeNBT(DaemonicaCapabilities.AFFILIATION, instance, null);
        }

        @Override
        public void deserializeNBT(NBTBase nbt) {
            DaemonicaCapabilities.AFFILIATION.getStorage().readNBT(DaemonicaCapabilities.AFFILIATION, instance, null, nbt);
        }

    }

    static Integer get(EntityPlayer player, Domain domain) {
        return player.hasCapability(DaemonicaCapabilities.AFFILIATION, null) ?
                player.getCapability(DaemonicaCapabilities.AFFILIATION, null).getAffiliation(domain) : 0;
    }

}
