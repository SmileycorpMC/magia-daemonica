package net.smileycorp.magiadaemonica.config;

import com.google.common.collect.Lists;
import net.minecraft.entity.EntityLiving;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.registries.GameData;

import java.io.File;
import java.util.List;

public class ItemsConfig {

    //alea diaboli
    public static int aleaDiaboliCooldown;
    public static int aleaDiaboliLuckMode;
    //beef suet
    public static int suetHunger;
    public static float suetSaturation;
    //calix perpetuus
    public static int calixPerpetuusCooldown;
    public static boolean calixPerpetuusMilk;
    //chalk stick
    public static int chalkDurability;
    public static boolean chalkEdible;
    public static int chalkHunger;
    public static float chalkSaturation;
    //facies excoriata
    public static int faciesExcoriataDuration;
    private static String[] faciesExcoriataEntityBlacklistStr;
    private static List<Class<? extends EntityLiving>> faciesExcoriataEntityBlacklist;
    public static float faciesExcoriataRange;
    public static int faciesExcoriataTickRate;
    //lavender petal
    public static int lavenderPetalHunger;
    public static float lavenderPetalSaturation;
    //lilac petal
    public static int lilacPetalHunger;
    public static float lilacPetalSaturation;
    //lorica aculeta
    public static int loricaAculetaBleedTime;
    public static float loricaAculetaDamageReflection;
    public static boolean loricaAculetaRemovable;
    //oak bark
    public static boolean oakBarkEdible;
    public static int oakBarkHunger;
    public static float oakBarkSaturation;
    //pumpkin slice
    public static int pumpkinSliceHunger;
    public static float pumpkinSliceSaturation;
    //peccatum primordiale
    public static float peccatumPrimordialeAffinityBoost;
    public static int peccatumPrimordialeDuration;
    private static String[] peccatumPrimordialeEffectBlacklistStr;
    public static int peccatumPrimordialeHunger;
    public static float peccatumPrimordialeSaturation;
    //peppermintLeaf
    public static int peppermintLeafHunger;
    public static float peppermintLeafSaturation;
    //rose petal
    public static int rosePetalHunger;
    public static float rosePetalSaturation;
    //spearmintLeaf
    public static int spearmintLeafHunger;
    public static float spearmintLeafSaturation;
    //tallow
    public static int tallowHunger;
    public static float tallowSaturation;
    //watermintLeaf
    public static int watermintLeafHunger;
    public static float watermintLeafSaturation;


    public static void syncConfig(FMLPreInitializationEvent event) {
        Configuration config = new Configuration(new File(event.getModConfigurationDirectory().getPath() + "/magiadaemonica/items.cfg"));
        try{
            config.load();
            aleaDiaboliCooldown = config.getInt("cooldown", "alea diaboli", 6000, 0, Integer.MAX_VALUE, "How long does the alea diaboli go on cooldown after use?");
            aleaDiaboliLuckMode = config.getInt("luckMode", "alea diaboli", 4, 0, 4, "How does the luck attribute effect alea diaboli rolls?" +
                    "\n0: luck doesn't affect rolls\n1: flat +1/-1 per full luck or negative luck level\n2: same as 1, with the remainder being a percent chance for an additional +1" +
                    "\n3: additional roll per full luck or negative luck level, with the highest (or lowest for negative luck) roll being chosen\n4: same as 3, with the remainder being a percent chance for an additional roll");
            //beef suet
            suetHunger = config.getInt("hunger", "beef suet", 1, 0, Integer.MAX_VALUE, "How much hunger does eating beef suet provide?");
            suetSaturation = config.getFloat("saturation", "beef suet", 0.2f, 0, Integer.MAX_VALUE, "How much saturation does eating beef suet provide?");
            //calix perpetuus
            calixPerpetuusCooldown = config.getInt("cooldown", "calix perpetuus", 60, 0, Integer.MAX_VALUE, "How long does the calix perpetuus go on cooldown after use?");
            calixPerpetuusMilk = config.getBoolean("canContainMilk", "calix perpetuus", true, "Can the calix perpetuus contain milk?");
            //chalk stick
            chalkDurability = config.getInt("durability", "chalk stick", 75, 0, Integer.MAX_VALUE, "How much durability do chalk sticks have?");
            chalkEdible = config.getBoolean("edible", "chalk stick", true, "Can chalk sticks be eaten?");
            chalkHunger = config.getInt("hunger", "chalk stick", 1, 0, Integer.MAX_VALUE, "How much hunger does eating a chalk stick provide?");
            chalkSaturation = config.getFloat("saturation", "chalk stick", 0.8f, 0, Integer.MAX_VALUE, "How much saturation does eating a chalk stick provide?");
            //facies excoriata
            faciesExcoriataEntityBlacklistStr = config.getStringList("entityBlacklist", "facies excoriata", new String[]{"minecraft:ender_dragon", "minecraft:wither"}, "Entities immune to the facies excoriata's petrify effect");
            faciesExcoriataDuration = config.getInt("duration", "facies excoriata", 40, 0, Integer.MAX_VALUE, "How long does the facies excoriata's petrify effect work on mobs after you stop looking ar them?");
            faciesExcoriataRange = config.getFloat("range", "facies excoriata", 32, 0, Float.MAX_VALUE, "How far away can the facies excoriata petrify mobs?");
            faciesExcoriataTickRate = config.getInt("tickRate", "facies excoriata", 10, 1, Integer.MAX_VALUE, "How often in ticks does the facies excoriata refresh it's petrify effect?");
            //lavender petal
            lavenderPetalHunger = config.getInt("hunger", "lavender petal", 1, 0, Integer.MAX_VALUE, "How much hunger does eating lavender petals provide?");
            lavenderPetalSaturation = config.getFloat("saturation", "lavender petal", 0.2f, 0, Integer.MAX_VALUE, "How much saturation does eating lavender petals provide?");
            //lavender petal
            lilacPetalHunger = config.getInt("hunger", "lilac petal", 1, 0, Integer.MAX_VALUE, "How much hunger does eating lilac petals provide?");
            lilacPetalSaturation = config.getFloat("saturation", "lilac petal", 0.2f, 0, Integer.MAX_VALUE, "How much saturation does eating lilac petals provide?");
            //lorica aculeta
            loricaAculetaBleedTime = config.getInt("bleedTime", "lorica aculeta", 60, 0, Integer.MAX_VALUE, "How long does the lorica aculeta inflict bleed for after reflecting damage");
            loricaAculetaDamageReflection = config.getFloat("damageReflection", "lorica aculeta", 0.3f, 0, 1, "Percentage of damage the lorica aculeta reduces damage by (before armour and resistance) and reflects back to the attacker");
            loricaAculetaRemovable = config.getBoolean("removable", "lorica aculeta", false, "Can the lorica aculeta be unequipped?");
            //oak bark
            oakBarkEdible = config.getBoolean("edible", "oak bark", true, "Can oak bark be eaten?");
            oakBarkHunger = config.getInt("hunger", "oak bark", 1, 0, Integer.MAX_VALUE, "How much hunger does eating oak bark provide?");
            oakBarkSaturation = config.getFloat("saturation", "oak bark", 0.2f, 0, Integer.MAX_VALUE, "How much saturation does eating oak bark provide?");
            //pumpkin slice
            pumpkinSliceHunger = config.getInt("hunger", "pumpkin slice", 2, 0, Integer.MAX_VALUE, "How much hunger does eating a pumpkin slice provide?");
            pumpkinSliceSaturation = config.getFloat("saturation", "pumpkin slice", 1.2f, 0, Integer.MAX_VALUE, "How much saturation does eating a pumpkin slice provide?");
            //peccatum primordiale
            peccatumPrimordialeAffinityBoost = config.getInt("affinityBoost", "peccatum primordiale", 1, 0, Integer.MAX_VALUE, "How much infernal affinity does eating the peccatum primordiale give? (infernal affinity increases the power of rituals by 1 for every 0.001 affinity)");
            peccatumPrimordialeDuration = config.getInt("duration", "peccatum primordiale", 200, 0, Integer.MAX_VALUE, "How many ticks do the peccatum primordiale's effects apply for?");
            peccatumPrimordialeEffectBlacklistStr = config.getStringList("effectBlacklist", "peccatum primordiale", new String[]{"magiadaemonica:petrified", "magiadaemonica:tremor"}, "Potion effects the peccatum primordiale will not apply when eaten");
            peccatumPrimordialeHunger = config.getInt("hunger", "peccatum primordiale", 4, 0, Integer.MAX_VALUE, "How much hunger does eating the peccatum primordiale provide?");
            peccatumPrimordialeSaturation = config.getFloat("saturation", "peccatum primordiale", 2.4f, 0, Integer.MAX_VALUE, "How much saturation does eating the peccatum primordiale provide?");
            //peppermintLeaf
            peppermintLeafHunger = config.getInt("hunger", "peppermint leaf", 3, 0, Integer.MAX_VALUE, "How much hunger does eating peppermint leaf provide?");
            peppermintLeafSaturation = config.getFloat("saturation", "peppermint leaf", 0.2f, 0, Integer.MAX_VALUE, "How much saturation does eating peppermint leaf provide?");
            //rose petal
            rosePetalHunger = config.getInt("hunger", "rose petal", 1, 0, Integer.MAX_VALUE, "How much hunger does eating rose petals provide?");
            rosePetalSaturation = config.getFloat("saturation", "rose petal", 0.2f, 0, Integer.MAX_VALUE, "How much saturation does eating rose petals provide?");
            //spearmintLeaf
            spearmintLeafHunger = config.getInt("hunger", "spearmint leaf", 3, 0, Integer.MAX_VALUE, "How much hunger does eating spearmint leaf provide?");
            spearmintLeafSaturation = config.getFloat("saturation", "spearmint leaf", 0.2f, 0, Integer.MAX_VALUE, "How much saturation does eating spearmint leaf provide?");
            //tallow
            tallowHunger = config.getInt("hunger", "tallow", 3, 0, Integer.MAX_VALUE, "How much hunger does eating tallow provide?");
            tallowSaturation = config.getFloat("saturation", "tallow", 0.4f, 0, Integer.MAX_VALUE, "How much saturation does eating tallow provide?");
            //watermintLeaf
            watermintLeafHunger = config.getInt("hunger", "watermint leaf", 3, 0, Integer.MAX_VALUE, "How much hunger does eating watermint leaf provide?");
            watermintLeafSaturation = config.getFloat("saturation", "watermint leaf", 0.2f, 0, Integer.MAX_VALUE, "How much saturation does eating watermint leaf provide?");
        } catch(Exception e) {
        } finally {
            if (config.hasChanged()) config.save();
        }
    }

    public static boolean canPeccatumPrimordialeApply(Potion potion) {
        for (String str : peccatumPrimordialeEffectBlacklistStr) if (str.equals(potion.getRegistryName().toString())) return false;
        return true;
    }

    public static boolean isImmuneToFaciesExcoriata(EntityLiving entity) {
        if (faciesExcoriataEntityBlacklist == null) {
                faciesExcoriataEntityBlacklist = Lists.newArrayList();
                for (String str : faciesExcoriataEntityBlacklistStr) {
                    try {
                        Class<?> clazz = null;
                        //check if it matches the syntax for a registry name
                        if (str.contains(":")) {
                            ResourceLocation loc = new ResourceLocation(str);
                            if (GameData.getEntityRegistry().containsKey(loc)) {
                                clazz = GameData.getEntityRegistry().getValue(loc).getEntityClass();
                            } else continue;
                        }
                        if (clazz == null) throw new Exception("Entry " + str + " is not in the correct format");
                        if (EntityLiving.class.isAssignableFrom(clazz)) {
                            faciesExcoriataEntityBlacklist.add((Class<? extends EntityLiving>) clazz);
                        } else {
                            throw new Exception("Entity " + str + " is not an instance of EntityLiving");
                        }
                    } catch (Exception e) {
                    }
                }
        }
        return faciesExcoriataEntityBlacklist.contains(entity.getClass());
    }

}
