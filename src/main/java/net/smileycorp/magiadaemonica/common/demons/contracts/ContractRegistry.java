package net.smileycorp.magiadaemonica.common.demons.contracts;

import com.google.common.collect.Maps;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.magiadaemonica.common.demons.contracts.costs.*;
import net.smileycorp.magiadaemonica.common.demons.contracts.offerings.AttributeOffering;
import net.smileycorp.magiadaemonica.common.demons.contracts.offerings.EffectOffering;
import net.smileycorp.magiadaemonica.common.demons.contracts.offerings.ItemOffering;
import net.smileycorp.magiadaemonica.common.demons.contracts.offerings.Offering;

import java.util.Map;

public class ContractRegistry {

    private static final Map<ResourceLocation, Cost.Reader> COSTS = Maps.newHashMap();
    private static final Map<ResourceLocation, Offering.Reader> OFFERINGS = Maps.newHashMap();

    public static void registerCost(ResourceLocation name, Cost.Reader reader) {
        COSTS.put(name, reader);
    }

    public static void registerOffering(ResourceLocation name, Offering.Reader reader) {
        OFFERINGS.put(name, reader);
    }

    public static void registerDefaults() {

        registerCost(ExperienceCost.ID, ExperienceCost::fromNBT);
        registerCost(ExperienceLevelCost.ID, ExperienceLevelCost::fromNBT);
        registerCost(HealthCost.ID, HealthCost::fromNBT);
        registerCost(ItemCost.ID, ItemCost::fromNBT);
        registerCost(SoulCost.ID, SoulCost::fromNBT);
        registerCost(SoulPercentCost.ID, SoulPercentCost::fromNBT);
        registerOffering(AttributeOffering.ID, AttributeOffering::fromNBT);
        registerOffering(EffectOffering.ID, EffectOffering::fromNBT);
        registerOffering(ItemOffering.ID, ItemOffering::fromNBT);
    }

    public static Cost readCost(NBTTagCompound cost) {
        Cost.Reader<?> reader = COSTS.get(new ResourceLocation(cost.getString("id")));
        return reader == null ? null : reader.apply(cost);
    }

    public static Offering readOffering(NBTTagCompound offering) {
        Offering.Reader<?> reader = OFFERINGS.get(new ResourceLocation(offering.getString("id")));
        return reader == null ? null : reader.apply(offering);
    }

}
