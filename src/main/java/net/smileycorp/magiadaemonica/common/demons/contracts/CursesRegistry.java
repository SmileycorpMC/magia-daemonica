package net.smileycorp.magiadaemonica.common.demons.contracts;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.capabilities.Curses;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class CursesRegistry {

    private static final Map<ResourceLocation, Integer> CURSES = Maps.newHashMap();
    
    public static final ResourceLocation WARPWORMS = register(Constants.loc("warpworms"), 1);
    public static final ResourceLocation KNIFESTEP = register(Constants.loc("knifestep"), 3);
    public static final ResourceLocation TORPIDITY = register(Constants.loc("torpidity"), 1);
    public static final ResourceLocation VORACITY = register(Constants.loc("voracity"), 3);
    public static final ResourceLocation LIGHTNINGROD = register(Constants.loc("lightningrod"), 4);
    public static final ResourceLocation HEMOPHILIA = register(Constants.loc("hemophilia"), 3);
    public static final ResourceLocation ICESOLES = register(Constants.loc("icesoles"), 1);
    public static final ResourceLocation CONSPICUOUS = register(Constants.loc("conspicuous"), 5);
    public static final ResourceLocation CORROSION = register(Constants.loc("corrosion"), 6);

    private static ResourceLocation register(ResourceLocation id, int maxLevel) {
        CURSES.put(id, maxLevel);
        return id;
    }

    public static Map<ResourceLocation, Integer> getCurses() {
        return CURSES;
    }

    public static Collection<ResourceLocation> getCurseNames() {
        return CURSES.keySet();
    }

    public static int getMaxLevel(ResourceLocation curse) {
        return CURSES.getOrDefault(curse, 0);
    }

    public static List<ResourceLocation> getApplicableCurses(EntityPlayer player) {
        return getCurseNames().stream().filter(curse -> !Curses.isMaxLevel(player, curse)).collect(Collectors.toList());
    }

    public static List<ResourceLocation> getRandomCurses(EntityPlayer player, int amount) {
        List<ResourceLocation> curses = Lists.newArrayList();
        if (amount == 0) return curses;
        List<ResourceLocation> applicable = getApplicableCurses(player);
        amount = Math.min(amount, applicable.size());
        Random rand = player.getRNG();
        for (int i = 0; i < amount; i++) {
            int r = rand.nextInt(applicable.size());
            curses.add(applicable.get(r));
            applicable.remove(r);
        }
        return curses;
    }

}
