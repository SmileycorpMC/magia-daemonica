package net.smileycorp.magiadaemonica.common.demons.contracts;

import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.magiadaemonica.common.Constants;

import java.util.Collection;
import java.util.Map;

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

}
