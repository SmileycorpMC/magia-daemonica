package net.smileycorp.magiadaemonica.common.demons.contracts.costs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentBase;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.capabilities.DaemonicaCapabilities;
import net.smileycorp.magiadaemonica.common.network.SyncSoulMessage;

public class SoulPercentCost implements Cost {

    public static ResourceLocation ID = Constants.loc("soul_percent");

    private final float amount;

    public SoulPercentCost(float amount) {
        this.amount = amount;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return ID;
    }

    @Override
    public void pay(EntityPlayer player, int tier) {
        if (!player.hasCapability(DaemonicaCapabilities.SOUL, null)) return;
        player.getCapability(DaemonicaCapabilities.SOUL, null).consumeSoul(amount, false);
        if (player instanceof EntityPlayerMP) SyncSoulMessage.send((EntityPlayerMP) player);
    }

    @Override
    public boolean canPay(EntityPlayer player, int tier) {
        if (!player.hasCapability(DaemonicaCapabilities.SOUL, null)) return false;
        return player.getCapability(DaemonicaCapabilities.SOUL, null).getSoul() > 0;
    }

    @Override
    public TextComponentBase getDescription(int tier) {
        return null;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt =new NBTTagCompound();
        nbt.setFloat("amount", amount);
        return nbt;
    }

    public static SoulPercentCost fromNBT(NBTTagCompound nbt) {
        return new SoulPercentCost(nbt.getFloat("amount"));
    }

}
