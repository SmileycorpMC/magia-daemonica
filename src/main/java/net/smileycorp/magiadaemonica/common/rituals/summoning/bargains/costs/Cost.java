package net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.costs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentBase;

public interface Cost {

    void pay(EntityPlayer player, int tier);

    boolean canPay(EntityPlayer player, int tier);

    TextComponentBase getDescription(int tier);

    NBTTagCompound writeToNBT();

    interface NBTReader<T extends Cost> {

        T apply(NBTTagCompound nbt);

    }

}
