package net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.costs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentBase;

public class ExperienceLevelCost implements Cost {

    private final int amount;

    public ExperienceLevelCost(int amount) {
        this.amount = amount;
    }

    @Override
    public void pay(EntityPlayer player, int tier) {
        player.experienceLevel -= amount;
    }

    @Override
    public boolean canPay(EntityPlayer player, int tier) {
        return player.experienceLevel >= amount;
    }

    @Override
    public TextComponentBase getDescription(int tier) {
        return null;
    }

}
