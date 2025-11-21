package net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.costs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentBase;
import net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.BargainUtils;

public class FlatExperienceCost implements Cost {

    private final int amount;

    public FlatExperienceCost(int amount) {
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

}
