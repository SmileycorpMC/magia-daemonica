package net.smileycorp.magiadaemonica.common.demons.contracts.costs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.demons.Demon;
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
    public void pay(EntityPlayer player) {
        ContractsUtils.takeExperience(player, amount);
    }

    @Override
    public boolean canPay(EntityPlayer player) {
        return player.experienceTotal >= amount;
    }

    @Override
    public Object[] getDescriptionArguments() {
        return new Object[]{amount};
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

    public static ExperienceCost generate(Demon demon, EntityPlayer player, int tier) {
        return new ExperienceCost((int) ((1000 + player.getRNG().nextGaussian() * 500) * tier));
    }

}
