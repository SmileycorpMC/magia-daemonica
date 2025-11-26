package net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.offerings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentBase;
import net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.costs.Cost;

public interface Offering {

    void grant(EntityPlayer player);

    TextComponentBase getDescription(int tier);

    NBTTagCompound writeToNBT();

    interface NBTReader<T extends Cost> {

        T apply(NBTTagCompound nbt);

    }

}
