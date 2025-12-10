package net.smileycorp.magiadaemonica.common.demons.contracts.costs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.capabilities.DaemonicaCapabilities;
import net.smileycorp.magiadaemonica.common.demons.Demon;
import net.smileycorp.magiadaemonica.common.demons.contracts.ContractsUtils;
import net.smileycorp.magiadaemonica.common.network.SyncSoulMessage;

public class SoulCost implements Cost {

    public static ResourceLocation ID = Constants.loc("soul");

    private final float amount;

    public SoulCost(float amount) {
        this.amount = ContractsUtils.round(amount, 4);
    }

    @Override
    public ResourceLocation getRegistryName() {
        return ID;
    }

    @Override
    public void pay(EntityPlayer player) {
        if (!player.hasCapability(DaemonicaCapabilities.SOUL, null)) return;
        player.getCapability(DaemonicaCapabilities.SOUL, null).consumeSoul(amount, true);
        if (player instanceof EntityPlayerMP) SyncSoulMessage.send((EntityPlayerMP) player);
    }

    @Override
    public boolean canPay(EntityPlayer player) {
        if (!player.hasCapability(DaemonicaCapabilities.SOUL, null)) return false;
        return player.getCapability(DaemonicaCapabilities.SOUL, null).getSoul() >= amount;
    }

    @Override
    public Object[] getDescriptionArguments() {
        return new Object[]{String.format("%.2f",amount * 100) + "%"};
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setFloat("amount", amount);
        return nbt;
    }

    public static SoulCost fromNBT(NBTTagCompound nbt) {
        return new SoulCost(nbt.getFloat("amount"));
    }

    public static SoulCost generate(Demon demon, EntityPlayer player, int tier) {
        return new SoulCost(MathHelper.clamp(tier * 0.075f + (float) player.getRNG().nextGaussian() * 0.05f, 0.01f, 1));
    }

}
