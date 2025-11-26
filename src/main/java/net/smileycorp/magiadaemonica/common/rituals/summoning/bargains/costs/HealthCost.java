package net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.costs;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentBase;
import net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.BargainUtils;

public class HealthCost implements Cost {

    private final float amount;

    public HealthCost(float amount) {
        this.amount = amount;
    }

    @Override
    public void pay(EntityPlayer player, int tier) {
        BargainUtils.addCostAttribute(player, SharedMonsterAttributes.MAX_HEALTH, amount);
    }

    @Override
    public boolean canPay(EntityPlayer player, int tier) {
        return player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue() > amount;
    }

    @Override
    public TextComponentBase getDescription(int tier) {
        return null;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setFloat("amount", amount);
        return nbt;
    }

    public static HealthCost fromNBT(NBTTagCompound nbt) {
        return new HealthCost(nbt.getFloat("amount"));
    }

}
