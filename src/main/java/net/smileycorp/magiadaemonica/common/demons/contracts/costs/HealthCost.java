package net.smileycorp.magiadaemonica.common.demons.contracts.costs;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.smileycorp.atlas.api.util.MathUtils;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.demons.Demon;
import net.smileycorp.magiadaemonica.common.demons.contracts.ContractsUtils;
import software.bernie.geckolib3.core.util.MathUtil;

public class HealthCost implements Cost {

    public static ResourceLocation ID = Constants.loc("health");

    private final float amount;

    public HealthCost(float amount) {
        this.amount = ContractsUtils.round(amount, 1);
    }

    @Override
    public ResourceLocation getRegistryName() {
        return ID;
    }

    @Override
    public void pay(EntityPlayer player) {
        ContractsUtils.addCostAttribute(player, SharedMonsterAttributes.MAX_HEALTH, amount);
    }

    @Override
    public boolean canPay(EntityPlayer player) {
        return player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getAttributeValue() > amount;
    }

    @Override
    public Object[] getDescriptionArguments() {
        return new Object[]{amount};
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

    public static HealthCost generate(Demon demon, EntityPlayer player, int tier) {
        return new HealthCost(MathHelper.clamp(0.5f * (int)(tier + 1 - (player.getRNG().nextInt(tier) * 0.5f)), 1, 10));
    }

}
