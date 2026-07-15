package net.smileycorp.magiadaemonica.common.demons.contracts;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.capabilities.Boons;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class BoonRegistry {

    private static final Map<ResourceLocation, Integer> BOONS = Maps.newHashMap();

    public static final ResourceLocation DEALMAKER = register(Constants.loc("dealmaker"), 3);
    public static final ResourceLocation SHATTERFIST = register(Constants.loc("shatterfist"), 3);
    public static final ResourceLocation HEMORRHAGE = register(Constants.loc("hemorrhage"), 4);
    public static final ResourceLocation JUGGERNAUT = register(Constants.loc("juggernaut"), 1);

    private static ResourceLocation register(ResourceLocation id, int maxLevel) {
        BOONS.put(id, maxLevel);
        return id;
    }

    public static Map<ResourceLocation, Integer> getBoons() {
        return BOONS;
    }

    public static Collection<ResourceLocation> getBoonNames() {
        return BOONS.keySet();
    }

    public static int getMaxLevel(ResourceLocation boon) {
        return BOONS.getOrDefault(boon, 0);
    }

    public static List<ResourceLocation> getApplicableBoon(EntityPlayer player) {
        return getBoonNames().stream().filter(boon -> !Boons.isMaxLevel(player, boon)).collect(Collectors.toList());
    }

    public static List<ResourceLocation> getRandomBoons(EntityPlayer player, int amount) {
        List<ResourceLocation> boons = Lists.newArrayList();
        if (amount == 0) return boons;
        List<ResourceLocation> applicable = getApplicableBoon(player);
        amount = Math.min(amount, applicable.size());
        Random rand = player.getRNG();
        for (int i = 0; i < amount; i++) {
            int r = rand.nextInt(applicable.size());
            boons.add(applicable.get(r));
            applicable.remove(r);
        }
        return boons;
    }

}
