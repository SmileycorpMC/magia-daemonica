package net.smileycorp.magiadaemonica.common.demons.contracts;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.atlas.api.util.Func;
import net.smileycorp.magiadaemonica.common.demons.Demon;
import net.smileycorp.magiadaemonica.common.demons.Rank;
import net.smileycorp.magiadaemonica.common.demons.contracts.costs.*;
import net.smileycorp.magiadaemonica.common.demons.contracts.offerings.*;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ContractRegistry {

    private static final Map<ResourceLocation, Cost.Entry<?>> COSTS = Maps.newHashMap();
    private static final Map<ResourceLocation, Offering.Entry<?>> OFFERINGS = Maps.newHashMap();

    public static <T extends Cost> void registerCost(ResourceLocation name, int tier, Cost.Reader<T> reader, Cost.Generator<T> generator, Function<ResourceLocation, Boolean> canApplyTogether) {
        COSTS.put(name, new Cost.Entry<>(name, tier, reader, generator, canApplyTogether));
    }

    public static <T extends Offering> void registerOffering(ResourceLocation name, int tier, Offering.Reader<T> reader, Offering.Generator<T> generator) {
        OFFERINGS.put(name, new Offering.Entry<>(name, tier, reader, generator));
    }

    public static void registerDefaults() {
        registerCost(ExperienceCost.ID, 1, ExperienceCost::fromNBT, ExperienceCost::generate, ExperienceCost::canApplyTogether);
        registerCost(ExperienceLevelCost.ID, 3, ExperienceLevelCost::fromNBT, ExperienceLevelCost::generate, ExperienceCost::canApplyTogether);
        registerCost(HealthCost.ID, 6, HealthCost::fromNBT, HealthCost::generate, func -> func != HealthCost.ID);
        registerCost(ItemCost.ID, 0, ItemCost::fromNBT, ItemCost::generate, Func::True);
        registerCost(SoulCost.ID, 4, SoulCost::fromNBT, SoulCost::generate, SoulCost::canApplyTogether);
        registerCost(SoulPercentageCost.ID, 3, SoulPercentageCost::fromNBT, SoulPercentageCost::generate, SoulCost::canApplyTogether);
        registerCost(CurseCost.ID, 5, CurseCost::fromNBT, CurseCost::generate, Func::True);
        //registerOffering(AttributeOffering.ID, 5, AttributeOffering::fromNBT);
        //registerOffering(EffectOffering.ID, 6, EffectOffering::fromNBT, EffectOffering::generate);
        registerOffering(BoonOffering.ID, 6, BoonOffering::fromNBT, BoonOffering::generate);
        registerOffering(ItemOffering.ID, 1, ItemOffering::fromNBT, ItemOffering::generate);
        registerOffering(ItemOffering.RELIC_ID, 6, ItemOffering::fromNBT, ItemOffering::generateRelic);
        registerOffering(ChoiceOffering.ID, 9, ChoiceOffering::fromNBT, ChoiceOffering::generate);
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
        tier += (int) ((double)tier * rand.nextGaussian() * 0.1);
        int searchTier = tier;
        while (true) {
            if (rand.nextFloat() > 0.1) break;
            searchTier++;
        }
        int costsCount = 1;
        for (int i = 0; i < tier; i++) if (rand.nextInt(10) == 0) costsCount++;
        Offering.Entry<?>[] offerings = OFFERINGS.values().toArray(new Offering.Entry[OFFERINGS.size()]);
        List<Cost.Entry<?>> costs = Lists.newArrayList(COSTS.values());
        Cost.Entry<?>[] contractCosts = new Cost.Entry[costsCount];
        Offering.Entry<?> offering = null;
        int tries = 0;
        for (int i = 0; i < contractCosts.length; i++) {
            tries = 0;
            while (tries++ <= 10) {
                Cost.Entry<?> cost = costs.get(rand.nextInt(costs.size()));
                contractCosts[i] = cost;
                if (!(demon.getDomain().isGreedy() && cost.getName().equals(ItemCost.ID))
                        && (cost.getTier() > searchTier || cost.getTier() > searchTier + 2)) continue;
                costs = costs.stream().filter(entry -> cost.canApplyTogether(entry.getName())).collect(Collectors.toList());
            }
        }
        tries = 0;
        while (tries ++ <= 10) {
            offering = offerings[rand.nextInt(offerings.length)];
            if (offering.getTier() <= searchTier) break;
        }
        for (Cost.Entry<?>cost : contractCosts) contract.addCosts(cost.getGenerator().apply(demon, player,
                (int) (Math.max((float) tier / (costsCount), 1))));
        contract.addOfferings(offering.getGenerator().apply(demon, player, tier));
        return contract;
    }

}
