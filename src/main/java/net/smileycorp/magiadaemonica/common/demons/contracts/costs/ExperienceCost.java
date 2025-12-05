package net.smileycorp.magiadaemonica.common.demons.contracts.costs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentBase;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.demons.contracts.ContractsUtils;

public class ExperienceCost implements Cost {

    public static ResourceLocation ID = Constants.loc("experience_level");

    private final int amount;

    public ExperienceCost(int amount) {
        this.amount = amount;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return ID;
    }

    @Override
    public void pay(EntityPlayer player, int tier) {
        ContractsUtils.takeExperience(player, amount);
    }

    @Override
    public boolean canPay(EntityPlayer player, int tier) {
        return player.experienceTotal >= amount;
    }

    @Override
    public TextComponentBase getDescription(int tier) {
        return null;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("amount", amount);
        return nbt;
    }

    public static ExperienceCost fromNBT(NBTTagCompound nbt) {
        return new ExperienceCost(nbt.getInteger("amount"));
    }

}
