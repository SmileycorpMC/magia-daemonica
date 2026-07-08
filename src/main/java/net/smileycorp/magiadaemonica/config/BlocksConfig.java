package net.smileycorp.magiadaemonica.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.block.BlockBookshelf;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;
import net.smileycorp.atlas.api.config.BlockStatEntry;
import net.smileycorp.atlas.api.util.RecipeUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class BlocksConfig {

    public static BlockStatEntry chalk;
    public static boolean rainExtinguishesCandles;
    public static BlockStatEntry scrollshelf;
    private static Set<Item> scrollshelfItems;
    private static String[] scrollshelfItemsStr;
    private static String[] scrollshelfItemsWords;
    private static String[] scrollshelfItemsBlacklist;

    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(new File(event.getModConfigurationDirectory().getPath() + "/magiadaemonica/blocks.cfg"));
        try{
            config.load();
            chalk = new BlockStatEntry(config, "chalk", 0.75, 0.75, 0);
            rainExtinguishesCandles = config.getBoolean("rainExtinguishesCandles", "scented candles", true, "Do candles chance to extinguish while it is raining?");
            scrollshelf = new BlockStatEntry(config, "scrollshelf", 1.5, 2.5, 0);
            scrollshelfItemsStr = config.getStringList("items", "scrollshelf",
                    new String[] {"futuremc:banner_pattern"},
                    "Items the scrollshelves can contain, in the format <modid>:<name>.");
            scrollshelfItemsWords = config.getStringList("itemWords", "scrollshelf",
                    new String[] {"scroll", "map"},
                    "Words that if item registry names contain they will automatically be placeable in scrollshelves.");
            scrollshelfItemsBlacklist = config.getStringList("itemsBlacklist", "scrollshelf",
                    new String[] {},
                    "Items scrollshelves can never contain, in the format <modid>:<name>.");
        } catch(Exception e) {
        } finally {
            if (config.hasChanged()) config.save();
        }
    }

    public static boolean canPlaceInScrollshelf(ItemStack stack) {
        if (stack.isEmpty()) return false;
        if (scrollshelfItems == null) {
            scrollshelfItems = Sets.newHashSet();
            ForgeRegistries.ITEMS.getValuesCollection().stream().forEach(item -> {
                if (scrollshelfItems.contains(item)) return;
                ResourceLocation loc = item.getRegistryName();
                if (loc == null) return;
                if (Arrays.stream(scrollshelfItemsBlacklist).anyMatch(str -> loc.toString().equals(str))) return;
                if (Arrays.stream(scrollshelfItemsStr).anyMatch(str -> loc.toString().equals(str))) {
                    scrollshelfItems.add(item);
                    return;
                }
                if (Arrays.stream(scrollshelfItemsWords).anyMatch(loc.getResourcePath()::contains)) scrollshelfItems.add(item);
            });
        }
        return scrollshelfItems.stream().anyMatch(item -> stack.getItem() == item);
    }

}
