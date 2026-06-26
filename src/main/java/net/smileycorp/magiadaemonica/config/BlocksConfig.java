package net.smileycorp.magiadaemonica.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.smileycorp.atlas.api.config.BiomeGenEntry;
import net.smileycorp.atlas.api.config.BlockStatEntry;
import net.smileycorp.atlas.api.config.WorldGenEntry;

import java.io.File;

public class BlocksConfig {

    public static BlockStatEntry chalk;
    public static boolean rainExtinguishesCandles;

    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(new File(event.getModConfigurationDirectory().getPath() + "/magiadaemonica/blocks.cfg"));
        try{
            config.load();
            chalk = new BlockStatEntry(config, "chalk", 0.75, 0.75, 0);
            rainExtinguishesCandles = config.getBoolean("rainExtinguishesCandles", "scented candles", true, "Do candles chance to extinguish while it is raining?");
        } catch(Exception e) {
        } finally {
            if (config.hasChanged()) config.save();
        }
    }

}
