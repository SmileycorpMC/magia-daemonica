package net.smileycorp.magiadaemonica.common.rituals.summoning.bargains;

import com.google.common.collect.Maps;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.costs.*;
import net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.offerings.AttributeOffering;
import net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.offerings.ItemOffering;
import net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.offerings.Offering;

import java.util.Map;

public class BargainRegistry {

    private static final Map<ResourceLocation, Cost.Reader> COSTS = Maps.newHashMap();
    private static final Map<ResourceLocation, Offering.Reader> OFFERINGS = Maps.newHashMap();

    public static void registerCost(ResourceLocation name, Cost.Reader reader) {
        COSTS.put(name, reader);
    }

    public static void registerOffering(ResourceLocation name, Offering.Reader reader) {
        OFFERINGS.put(name, reader);
    }

    public static void registerDefaults() {
        registerCost(Constants.loc("experience_level"), ExperienceLevelCost::fromNBT);
        registerCost(Constants.loc("experience"), ExperienceCost::fromNBT);
        registerCost(Constants.loc("health"), HealthCost::fromNBT);
        registerCost(Constants.loc("item"), ItemCost::fromNBT);
        registerCost(Constants.loc("soul"), SoulCost::fromNBT);
        registerCost(Constants.loc("soul_percent"), SoulPercentCost::fromNBT);
        registerOffering(Constants.loc("item"), ItemOffering::fromNBT);
        registerOffering(Constants.loc("attribute"), AttributeOffering::fromNBT);
    }

}
