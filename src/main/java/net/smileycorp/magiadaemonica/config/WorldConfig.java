package net.smileycorp.magiadaemonica.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.smileycorp.atlas.api.config.BiomeGenEntry;
import net.smileycorp.atlas.api.config.WorldGenEntry;

import java.io.File;

public class WorldConfig {

    public static WorldGenEntry chalk;
    public static BiomeGenEntry chalkBiomes;
    public static int[] lavenderDimensions;
    public static int lavenderSpawnChance;
    public static int lavenderMinSize;
    public static int lavenderMaxSize;
    public static BiomeGenEntry lavenderBiomes;
    public static int[] peppermintDimensions;
    public static int peppermintSpawnChance;
    public static int peppermintMinSize;
    public static int peppermintMaxSize;
    public static BiomeGenEntry peppermintBiomes;

    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(new File(event.getModConfigurationDirectory().getPath() + "/magiadaemonica/world.cfg"));
        try{
            config.load();
            //chalk
            chalk = new WorldGenEntry(config, "chalk", 33, 2, 55, 75, new int[]{0});
            chalkBiomes = new BiomeGenEntry(config, "chalk", new String[]{"BEACH", "OCEAN", "PLAINS", "FOREST"}, new String[0]);
            //lavender
            lavenderDimensions = config.get("lavender", "dimensions", new int[]{0}, "Which dimensions can lavender generate in?").getIntList();
            lavenderSpawnChance = config.get("lavender", "spawnChances", 20, "Chance for lavender to spawn per chunk (Set to 0 to disable generation)").getInt();
            lavenderMinSize = config.get("lavender", "minSize", 3, "Minimum amount of lavender to spawn per patch").getInt();
            lavenderMaxSize = config.get("lavender", "maxSize", 7, "Maximum amount of lavender to spawn per patch.").getInt();
            lavenderBiomes = new BiomeGenEntry(config, "lavender", new String[]{"PLAINS"}, new String[0]);
            //peppermint
            peppermintDimensions = config.get("peppermint", "dimensions", new int[]{0}, "Which dimensions can peppermint generate in?").getIntList();
            peppermintSpawnChance = config.get("peppermint", "spawnChances", 20, "Chance for peppermint to spawn per chunk (Set to 0 to disable generation)").getInt();
            peppermintMinSize = config.get("peppermint", "minSize", 3, "Minimum amount of peppermint to spawn per patch").getInt();
            peppermintMaxSize = config.get("peppermint", "maxSize", 7, "Maximum amount of peppermint to spawn per patch.").getInt();
            peppermintBiomes = new BiomeGenEntry(config, "peppermint", new String[]{"RIVER", "FOREST"}, new String[0]);
        } catch(Exception e) {
        } finally {
            if (config.hasChanged()) config.save();
        }
    }


}
