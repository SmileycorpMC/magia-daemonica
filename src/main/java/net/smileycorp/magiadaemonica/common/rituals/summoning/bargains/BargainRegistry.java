package net.smileycorp.magiadaemonica.common.rituals.summoning.bargains;

import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.costs.*;
import net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.offerings.Offering;

import java.util.Map;

public class BargainRegistry {

    private static final Map<ResourceLocation, Cost.NBTReader> COSTS = Maps.newHashMap();
    private static final Map<ResourceLocation, Offering.NBTReader> OFFERINGS = Maps.newHashMap();

    public static void registerCost(ResourceLocation name, Cost.NBTReader reader) {
        COSTS.put(name, reader);
    }

    public static void registerOfferings(ResourceLocation name, Offering.NBTReader reader) {
        OFFERINGS.put(name, reader);
    }

    public static void registerDefaults() {
        registerCost(Constants.loc("experience_level"), ExperienceLevelCost::fromNBT);
        registerCost(Constants.loc("experience"), ExperienceCost::fromNBT);
        registerCost(Constants.loc("health"), HealthCost::fromNBT);
        registerCost(Constants.loc("item"), ItemCost::fromNBT);
        registerCost(Constants.loc("soul"), SoulCost::fromNBT);
        registerCost(Constants.loc("soul_percent"), SoulPercentCost::fromNBT);
    }

}
