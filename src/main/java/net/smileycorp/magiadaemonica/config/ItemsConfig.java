package net.smileycorp.magiadaemonica.config;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.io.File;

public class ItemsConfig {

    public static float loricaAculetaDamageReflection;
    public static int loricaAculetaBleedTime;

    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(new File(event.getModConfigurationDirectory().getPath() + "/magiadaemonica/blocks.cfg"));
        try{
            config.load();
            loricaAculetaDamageReflection = config.getFloat("damageReflection", "Aculeta", 0.3f, 0, 1, "Percentage of damage the Lorica Aculeta reduces damage by (before armour and resistance) and reflects back to the attacker");
            loricaAculetaBleedTime = config.getInt("bleedTime", "Aculeta", 60, 0, Integer.MAX_VALUE, "How long does the Lorica Aculeta inflict bleed for after reflecting damage");
        } catch(Exception e) {
        } finally {
            if (config.hasChanged()) config.save();
        }
    }

}
