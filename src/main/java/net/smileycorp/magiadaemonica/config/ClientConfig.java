package net.smileycorp.magiadaemonica.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ClientConfig {

    public static float tremorShakeIntensity;

    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(new File(event.getModConfigurationDirectory().getPath() + "/magiadaemonica/client.cfg"));
        try{
            tremorShakeIntensity = config.getFloat("tremorShakeIntensity", "accessibility", 1, 0, 3, "How intense is the camera shake from the tremor effect?");
        } catch(Exception e) {
        } finally {
            if (config.hasChanged()) config.save();
        }
    }

}
