package net.smileycorp.magiadaemonica.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.smileycorp.atlas.api.config.BiomeGenEntry;
import net.smileycorp.atlas.api.config.WorldGenEntry;

import java.io.File;

public class WorldConfig {

    //chalk
    public static WorldGenEntry chalk;
    public static BiomeGenEntry chalkBiomes;

    //lavender
    public static int[] lavenderDimensions;
    public static int lavenderSpawnChance;
    public static int lavenderMinSize;
    public static int lavenderMaxSize;
    public static BiomeGenEntry lavenderBiomes;

    //spearmint
    public static int[] spearmintDimensions;
    public static int spearmintSpawnChance;
    public static int spearmintMinSize;
    public static int spearmintMaxSize;
    public static BiomeGenEntry spearmintBiomes;
    public static double spearmintPeppermintChance;

    //watermint
    public static int[] watermintDimensions;
    public static int watermintSpawnChance;
    public static int watermintMinSize;
    public static int watermintMaxSize;
    public static BiomeGenEntry watermintBiomes;
    public static double watermintPeppermintChance;

    //peppermint
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
            lavenderSpawnChance = config.get("lavender", "spawnChances", 25, "Chance for lavender to spawn per chunk (Set to 0 to disable generation)").getInt();
            lavenderMinSize = config.get("lavender", "minSize", 3, "Minimum amount of lavender to spawn per patch").getInt();
            lavenderMaxSize = config.get("lavender", "maxSize", 7, "Maximum amount of lavender to spawn per patch.").getInt();
            lavenderBiomes = new BiomeGenEntry(config, "lavender", new String[]{"PLAINS", "FOREST"}, new String[0]);

            //spearmint
            spearmintDimensions = config.get("spearmint", "dimensions", new int[]{0}, "Which dimensions can spearmint generate in?").getIntList();
            spearmintSpawnChance = config.get("spearmint", "spawnChances", 20, "Chance for spearmint to spawn per chunk (Set to 0 to disable generation)").getInt();
            spearmintMinSize = config.get("spearmint", "minSize", 3, "Minimum amount of spearmint to spawn per patch").getInt();
            spearmintMaxSize = config.get("spearmint", "maxSize", 7, "Maximum amount of spearmint to spawn per patch.").getInt();
            spearmintBiomes = new BiomeGenEntry(config, "spearmint", new String[]{"PLAINS", "FOREST", "HILLS"}, new String[0]);
            spearmintPeppermintChance = config.get("spearmint", "peppermintChance", 0.3, "Chance for a spearmint to generate as peppermint instead.", 0, 1).getDouble();

            //watermint
            watermintDimensions = config.get("watermint", "dimensions", new int[]{0}, "Which dimensions can watermint generate in?").getIntList();
            watermintSpawnChance = config.get("watermint", "spawnChances", 20, "Chance for watermint to spawn per chunk (Set to 0 to disable generation)").getInt();
            watermintMinSize = config.get("watermint", "minSize", 3, "Minimum amount of watermint to spawn per patch").getInt();
            watermintMaxSize = config.get("watermint", "maxSize", 7, "Maximum amount of watermint to spawn per patch.").getInt();
            watermintBiomes = new BiomeGenEntry(config, "watermint", new String[]{"RIVER", "SWAMP"}, new String[0]);
            watermintPeppermintChance = config.get("watermint", "peppermintChance", 0.3, "Chance for a watermint to generate as peppermint instead.", 0, 1).getDouble();

            //peppermint
            peppermintDimensions = config.get("peppermint", "dimensions", new int[]{0}, "Which dimensions can peppermint generate in?").getIntList();
            peppermintSpawnChance = config.get("peppermint", "spawnChances", 5, "Chance for peppermint to spawn per chunk (Set to 0 to disable generation)").getInt();
            peppermintMinSize = config.get("peppermint", "minSize", 3, "Minimum amount of peppermint to spawn per patch").getInt();
            peppermintMaxSize = config.get("peppermint", "maxSize", 7, "Maximum amount of peppermint to spawn per patch.").getInt();
            peppermintBiomes = new BiomeGenEntry(config, "peppermint", new String[]{"RIVER", "SWAMP", "PLAINS"}, new String[0]);
        } catch(Exception e) {
        } finally {
            if (config.hasChanged()) config.save();
        }
    }

}
