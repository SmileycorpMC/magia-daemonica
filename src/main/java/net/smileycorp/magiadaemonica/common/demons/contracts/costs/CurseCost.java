package net.smileycorp.magiadaemonica.common.demons.contracts.costs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.capabilities.Curses;
import net.smileycorp.magiadaemonica.common.capabilities.DaemonicaCapabilities;
import net.smileycorp.magiadaemonica.common.demons.Demon;
import net.smileycorp.magiadaemonica.common.demons.contracts.CursesRegistry;

import java.util.List;
import java.util.stream.Collectors;

public class CurseCost implements Cost {

    public static ResourceLocation ID = Constants.loc("curse");

    private final ResourceLocation curse;

    public CurseCost(ResourceLocation curse) {
        this.curse = curse;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return ID;
    }

    @Override
    public void pay(EntityPlayer player) {
        if (!player.hasCapability(DaemonicaCapabilities.CURSES, null)) return;
        Curses.add(player, curse);
    }

    @Override
    public boolean canPay(EntityPlayer player) {
        if (!player.hasCapability(DaemonicaCapabilities.CURSES, null)) return false;
        return Curses.getLevel(player, curse) < CursesRegistry.getMaxLevel(curse);
    }

    @Override
    public Object[] getDescriptionArguments() {
        String key = "curse." + curse.getResourceDomain() + "." + curse.getResourcePath();
        return new Object[]{new TextComponentTranslation(key),
                new TextComponentTranslation(key + ".description")};
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("curse", curse.toString());
        return nbt;
    }

    public static CurseCost fromNBT(NBTTagCompound nbt) {
        return new CurseCost(new ResourceLocation(nbt.getString("curse")));
    }

    public static CurseCost generate(Demon demon, EntityPlayer player, int tier) {
        List<ResourceLocation> curses = CursesRegistry.getCurses().keySet().stream()
                .filter(curse -> !Curses.isMaxLevel(player, curse)).collect(Collectors.toList());
        if (curses.isEmpty()) curses.addAll(CursesRegistry.getCurses().keySet());
        return new CurseCost(curses.get(player.getRNG().nextInt(curses.size())));
    }

}
