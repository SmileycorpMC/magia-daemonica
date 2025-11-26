package net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.costs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentBase;
import net.smileycorp.magiadaemonica.common.capabilities.DaemonicaCapabilities;
import net.smileycorp.magiadaemonica.common.network.SyncSoulMessage;

public class SoulCost implements Cost {

    private final float amount;

    public SoulCost(float amount) {
        this.amount = amount;
    }

    @Override
    public void pay(EntityPlayer player, int tier) {
        if (!player.hasCapability(DaemonicaCapabilities.SOUL, null)) return;
        player.getCapability(DaemonicaCapabilities.SOUL, null).consumeSoul(amount, true);
        if (player instanceof EntityPlayerMP) SyncSoulMessage.send((EntityPlayerMP) player);
    }

    @Override
    public boolean canPay(EntityPlayer player, int tier) {
        if (!player.hasCapability(DaemonicaCapabilities.SOUL, null)) return false;
        return player.getCapability(DaemonicaCapabilities.SOUL, null).getSoul() >= amount;
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

    public static SoulCost fromNBT(NBTTagCompound nbt) {
        return new SoulCost(nbt.getFloat("amount"));
    }

}
