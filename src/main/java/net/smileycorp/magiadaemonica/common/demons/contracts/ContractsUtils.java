package net.smileycorp.magiadaemonica.common.demons.contracts;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.smileycorp.magiadaemonica.common.DaemonicaAttributes;
import net.smileycorp.magiadaemonica.common.demons.Demon;
import net.smileycorp.magiadaemonica.common.demons.Rank;
import net.smileycorp.magiadaemonica.common.items.ItemRelic;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class ContractsUtils {

    private static final UUID COST = UUID.fromString("cdc852af-afe8-4eac-85bc-3f90e670a8da");
    private static final UUID BONUS = UUID.fromString("77f9ece5-8597-4b7a-8705-898598878e06");
    private static final UUID SUMMON_AFFINITY = UUID.fromString("6143429d-3091-47dd-a1ff-d2116f53fb35");

    private static Map<EnumRarity, List<ItemStack>> ITEMS_BY_RARITY;
    private static List<ItemStack> ENCHANTABLE_ITEMS;
    private static List<ItemStack> FOOD;
    private static List<ItemStack> RELICS;

    public static void addCostAttribute(EntityPlayer player, IAttribute attribute, double value) {
        IAttributeInstance attributes = player.getEntityAttribute(attribute);
        AttributeModifier modifier = attributes.getModifier(COST);
        if (modifier != null) {
            value -= modifier.getAmount();
            attributes.removeModifier(modifier);
        }
        attributes.applyModifier(new AttributeModifier(COST, "magiadaemonicacosts", value, 0));
    }

    public static void addBonusAttribute(EntityPlayer player, String attribute, double value) {
        AbstractAttributeMap map = player.getAttributeMap();
        IAttributeInstance attributes = map.getAttributeInstanceByName(attribute);
        if (attributes == null) attributes = map.registerAttribute(new RangedAttribute(null, attribute, 0, -Double.MAX_VALUE, Double.MAX_VALUE));
        AttributeModifier modifier = attributes.getModifier(BONUS);
        if (modifier != null) {
            value += modifier.getAmount();
            attributes.removeModifier(modifier);
        }
        attributes.applyModifier(new AttributeModifier(BONUS, "magiadaemonicabonus", value, 0));
    }

    public static void takeExperience(EntityPlayer player, int amount) {
        while (amount > 0) {
            int cap = xpBarCap(player.experienceLevel);
            int xp = (int) (player.experience * cap);
            xp-= amount;
            if (xp > 0) {
                player.experience = (float) xp / (float) cap;
                return;
            }
            amount = -xp;
            if (amount > 0) {
                player.experienceLevel -= 1;
                cap = xpBarCap(player.experienceLevel);
                player.experience = (float) (cap - 1)/ (float) cap;
            }
        }
    }

    public static int xpBarCap(int xp) {
        return xp >= 30 ? 112 + (xp - 30) * 9 :
            xp >= 15 ? 37 + (xp - 15) * 5 : 7 + xp * 2;
    }

    public static int getContractCount(Demon demon, EntityPlayer player) {
        int contracts = demon == null ? 1 : demon.getRank().getBaseContractCount();
        while (true) {
            if (player.getRNG().nextFloat() > 0.1) break;
            contracts += 1;
        }
        return contracts;
    }

    private static void buildItemLists() {
        Item[] blacklist = {Items.SPAWN_EGG, Items.FILLED_MAP, Items.POTIONITEM, Items.SPLASH_POTION, Items.LINGERING_POTION,
            Items.TIPPED_ARROW, Item.getItemFromBlock(Blocks.BEDROCK), Item.getItemFromBlock(Blocks.END_PORTAL_FRAME)};
        ITEMS_BY_RARITY = Maps.newEnumMap(EnumRarity.class);
        for (EnumRarity rarity : EnumRarity.values()) ITEMS_BY_RARITY.put(rarity, Lists.newArrayList());
        ENCHANTABLE_ITEMS = Lists.newArrayList();
        FOOD = Lists.newArrayList();
        RELICS = Lists.newArrayList();
        for (Item item : ForgeRegistries.ITEMS) {
            if (item instanceof ItemRelic) {
                RELICS.add(new ItemStack(item));
                continue;
            }
            ItemStack stack = new ItemStack(item);
            if (stack.getItem() == Items.ENCHANTED_BOOK) {
                ENCHANTABLE_ITEMS.add(new ItemStack(Items.BOOK));
                continue;
            }
            if (item.isEnchantable(stack) && stack.getMaxStackSize() == 1) {
                ENCHANTABLE_ITEMS.add(new ItemStack(item));
                continue;
            }
            for (Item banned : blacklist) if (item == banned) continue;
            NonNullList<ItemStack> stacks = NonNullList.create();
            item.getSubItems(CreativeTabs.SEARCH, stacks);
            if (item instanceof ItemFood) {
                for (ItemStack stack1 : stacks) {
                    FOOD.add(stack1);
                    IRarity rarity = item.getForgeRarity(stack1);
                    if (!(rarity instanceof EnumRarity) || rarity == EnumRarity.COMMON) continue;
                    ITEMS_BY_RARITY.get((EnumRarity) rarity).add(stack1);
                }
                continue;
            }
            for (ItemStack stack1 : stacks) {
                IRarity rarity = item.getForgeRarity(stack1);
                if (!(rarity instanceof EnumRarity)) continue;
                ITEMS_BY_RARITY.get((EnumRarity) rarity).add(stack1);
            }
        }
    }

    public static ItemStack getItem(EnumRarity rarity, Random rand) {
        if (ITEMS_BY_RARITY == null) buildItemLists();
        List<ItemStack> items = ITEMS_BY_RARITY.get(rarity);
        if (items.isEmpty()) return getItem(EnumRarity.values()[rarity.ordinal() - 1], rand);
        return items.get(rand.nextInt(items.size())).copy();
    }

    public static ItemStack getEnchantableItem(Random rand) {
        if (ENCHANTABLE_ITEMS == null) buildItemLists();
        return ENCHANTABLE_ITEMS.get(rand.nextInt(ENCHANTABLE_ITEMS.size())).copy();
    }

    public static ItemStack getFood(Random rand) {
        if (FOOD == null) buildItemLists();
        return FOOD.get(rand.nextInt(FOOD.size())).copy();
    }

    public static ItemStack getRelic(Random rand) {
        if (RELICS == null) buildItemLists();
        return RELICS.get(rand.nextInt(RELICS.size())).copy();
    }

    public static float round(float amount, int places) {
        return BigDecimal.valueOf(amount).setScale(places, RoundingMode.HALF_UP).floatValue();
    }

    public static void addAffinity(EntityPlayerMP player, Demon demon) {
        IAttributeInstance attributes = player.getEntityAttribute(DaemonicaAttributes.INFERNAL_AFFINITY);
        AttributeModifier modifier = attributes.getModifier(BONUS);
        double value = 0.005 * (Rank.values().length - demon.getRank().ordinal() + 1);
        if (modifier != null) {
            value += modifier.getAmount();
            attributes.removeModifier(modifier);
        }
        attributes.applyModifier(new AttributeModifier(SUMMON_AFFINITY, "summon_affinity", value, 0));
    }

}
