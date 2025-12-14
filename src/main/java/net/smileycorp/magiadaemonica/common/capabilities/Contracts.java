package net.smileycorp.magiadaemonica.common.capabilities;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.smileycorp.magiadaemonica.common.demons.contracts.Contract;

import javax.annotation.Nullable;
import java.util.List;

public interface Contracts {

    void addContract(Contract contract);

    void readFromNBT(NBTTagList nbt);

    NBTTagList writeToNBT();

    class Impl implements Contracts {

        private final List<Contract> contracts = Lists.newArrayList();

        @Override
        public void addContract(Contract contract) {
            contracts.add(contract);
        }

        @Override
        public void readFromNBT(NBTTagList nbt) {
            contracts.clear();
            for (NBTBase nbtBase : nbt) {
                contracts.add(Contract.readFromNBT((NBTTagCompound) nbtBase));
            }
        }

        @Override
        public NBTTagList writeToNBT() {
            NBTTagList nbt = new NBTTagList();
            for (Contract contract : contracts) nbt.appendTag(contract.writeToNBT());
            return nbt;
        }

    }

    class Storage implements Capability.IStorage<Contracts> {

        @Nullable
        @Override
        public NBTBase writeNBT(Capability<Contracts> capability, Contracts contracts, EnumFacing enumFacing) {
            return contracts.writeToNBT();
        }

        @Override
        public void readNBT(Capability<Contracts> capability, Contracts contracts, EnumFacing enumFacing, NBTBase nbt) {
            contracts.readFromNBT((NBTTagList)nbt);
        }

    }

    class Provider implements ICapabilitySerializable<NBTBase> {

        protected Contracts instance = DaemonicaCapabilities.CONTRACTS.getDefaultInstance();

        @Override
        public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
            return capability == DaemonicaCapabilities.CONTRACTS;
        }

        @Override
        public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
            return capability == DaemonicaCapabilities.CONTRACTS ? DaemonicaCapabilities.CONTRACTS.cast(instance) : null;
        }

        @Override
        public NBTBase serializeNBT() {
            return DaemonicaCapabilities.CONTRACTS.getStorage().writeNBT(DaemonicaCapabilities.CONTRACTS, instance, null);
        }

        @Override
        public void deserializeNBT(NBTBase nbt) {
            DaemonicaCapabilities.CONTRACTS.getStorage().readNBT(DaemonicaCapabilities.CONTRACTS, instance, null, nbt);
        }

    }

}
