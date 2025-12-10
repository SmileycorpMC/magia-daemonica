package net.smileycorp.magiadaemonica.common.demons.contracts.costs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.demons.Demon;

public class ExperienceLevelCost implements Cost {

    public static ResourceLocation ID = Constants.loc("experience_level");

    private final int amount;

    public ExperienceLevelCost(int amount) {
        this.amount = amount;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return ID;
    }

    @Override
    public void pay(EntityPlayer player) {
        player.experienceLevel -= amount;
    }

    @Override
    public boolean canPay(EntityPlayer player) {
        return player.experienceLevel >= amount;
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

    public static ExperienceLevelCost fromNBT(NBTTagCompound nbt) {
        return new ExperienceLevelCost(nbt.getInteger("amount"));
    }

    public static ExperienceLevelCost generate(Demon demon, EntityPlayer player, int tier) {
        return new ExperienceLevelCost(player.getRNG().nextInt(tier + 10) + tier * 3);
    }

}
