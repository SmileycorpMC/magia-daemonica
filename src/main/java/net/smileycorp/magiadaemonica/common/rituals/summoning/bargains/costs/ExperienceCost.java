package net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.costs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentBase;
import net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.BargainUtils;

public class ExperienceCost implements Cost {

    private final int amount;

    public ExperienceCost(int amount) {
        this.amount = amount;
    }

    @Override
    public void pay(EntityPlayer player, int tier) {
        BargainUtils.takeExperience(player, amount);
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
