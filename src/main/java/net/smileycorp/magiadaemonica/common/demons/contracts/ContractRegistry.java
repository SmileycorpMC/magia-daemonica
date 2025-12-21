package net.smileycorp.magiadaemonica.common.demons.contracts;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.magiadaemonica.common.demons.Demon;
import net.smileycorp.magiadaemonica.common.demons.Rank;
import net.smileycorp.magiadaemonica.common.demons.contracts.costs.*;
import net.smileycorp.magiadaemonica.common.demons.contracts.offerings.EffectOffering;
import net.smileycorp.magiadaemonica.common.demons.contracts.offerings.ItemOffering;
import net.smileycorp.magiadaemonica.common.demons.contracts.offerings.Offering;

import java.util.Map;
import java.util.Random;

public class ContractRegistry {

    private static final Map<ResourceLocation, Cost.Entry<?>> COSTS = Maps.newHashMap();
    private static final Map<ResourceLocation, Offering.Entry<?>> OFFERINGS = Maps.newHashMap();

    public static <T extends Cost> void registerCost(ResourceLocation name, int tier, Cost.Reader<T> reader, Cost.Generator<T> generator) {
        COSTS.put(name, new Cost.Entry<>(name, tier, reader, generator));
    }

    public static <T extends Offering> void registerOffering(ResourceLocation name, int tier, Offering.Reader<T> reader, Offering.Generator<T> generator) {
        OFFERINGS.put(name, new Offering.Entry<>(name, tier, reader, generator));
    }

    public static void registerDefaults() {
        registerCost(ExperienceCost.ID, 1, ExperienceCost::fromNBT, ExperienceCost::generate);
        registerCost(ExperienceLevelCost.ID, 3, ExperienceLevelCost::fromNBT, ExperienceLevelCost::generate);
        registerCost(HealthCost.ID, 6, HealthCost::fromNBT, HealthCost::generate);
        registerCost(ItemCost.ID, 0, ItemCost::fromNBT, ItemCost::generate);
        registerCost(SoulCost.ID, 4, SoulCost::fromNBT, SoulCost::generate);
        registerCost(SoulPercentageCost.ID, 3, SoulPercentageCost::fromNBT, SoulPercentageCost::generate);
        //registerOffering(AttributeOffering.ID, 5, AttributeOffering::fromNBT);
        registerOffering(EffectOffering.ID, 6, EffectOffering::fromNBT, EffectOffering::generate);
        registerOffering(ItemOffering.ID, 1, ItemOffering::fromNBT, ItemOffering::generate);
        registerOffering(ItemOffering.RELIC_ID, 6, ItemOffering::fromNBT, ItemOffering::generateRelic);
    }

    public static Cost readCost(NBTTagCompound cost) {
        Cost.Entry<?> entry = COSTS.get(new ResourceLocation(cost.getString("id")));
        return entry == null ? null : entry.getReader().apply(cost);
    }

    public static Offering readOffering(NBTTagCompound offering) {
        Offering.Entry<?> entry = OFFERINGS.get(new ResourceLocation(offering.getString("id")));
        return entry == null ? null : entry.getReader().apply(offering);
    }

    public static Contract generateContract(Demon demon, EntityPlayer player) {
        Contract contract = new Contract(demon.getUUID());
        Random rand = player.getRNG();
        int tier = Rank.values().length - demon.getRank().ordinal();
        tier += tier * rand.nextGaussian() * 0.1;
        int searchTier = tier;
        while (true) {
            if (rand.nextFloat() > 0.1) break;
            searchTier++;
        }
        Offering.Entry<?>[] offerings = OFFERINGS.values().toArray(new Offering.Entry[OFFERINGS.size()]);
        Cost.Entry<?>[] costs = COSTS.values().toArray(new Cost.Entry[COSTS.size()]);
        Cost.Entry<?> cost = null;
        Offering.Entry<?> offering = null;
        int tries = 0;
        while (tries++ <= 10) {
            cost = costs[rand.nextInt(costs.length)];
            offering = offerings[rand.nextInt(offerings.length)];
            if (!(demon.getDomain().isGreedy() && cost.getName().equals(ItemCost.ID))
                    && (cost.getTier() > searchTier || cost.getTier() > searchTier + 2)) continue;
            if (offering.getTier() <= searchTier) break;
        }
        contract.addCosts(cost.getGenerator().apply(demon, player, tier));
        contract.addOfferings(offering.getGenerator().apply(demon, player, tier));
        return contract;
    }

}
