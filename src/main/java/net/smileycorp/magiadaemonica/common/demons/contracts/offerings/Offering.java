package net.smileycorp.magiadaemonica.common.demons.contracts.offerings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentBase;

public interface Offering {

    ResourceLocation getRegistryName();

    void grant(EntityPlayer player);

    TextComponentBase getDescription(int tier);

    NBTTagCompound writeToNBT();

    static NBTTagCompound writeToNBT(Offering offering) {
        NBTTagCompound nbt = offering.writeToNBT();
        nbt.setString("id", offering.getRegistryName().toString());
        return nbt;
    }

    interface Reader<T extends Offering> {

        T apply(NBTTagCompound nbt);

    }

}
