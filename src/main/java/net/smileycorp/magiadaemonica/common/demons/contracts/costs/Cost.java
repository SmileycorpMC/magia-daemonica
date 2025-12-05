package net.smileycorp.magiadaemonica.common.demons.contracts.costs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentBase;

public interface Cost {

    ResourceLocation getRegistryName();

    void pay(EntityPlayer player, int tier);

    boolean canPay(EntityPlayer player, int tier);

    TextComponentBase getDescription(int tier);

    NBTTagCompound writeToNBT();

    static NBTTagCompound writeToNBT(Cost cost) {
        NBTTagCompound nbt = cost.writeToNBT();
        nbt.setString("id", cost.getRegistryName().toString());
        return nbt;
    }

    interface Reader<T extends Cost> {

        T apply(NBTTagCompound nbt);

    }

}
