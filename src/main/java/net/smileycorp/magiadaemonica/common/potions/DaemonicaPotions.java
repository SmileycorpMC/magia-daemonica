package net.smileycorp.magiadaemonica.common.potions;

import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.magiadaemonica.common.Constants;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class DaemonicaPotions {

    public static final Potion TREMOR = new DaemonicaPotion(true, 0xD55E3C, "tremor");
    public static final Potion PETRIFIED = new DaemonicaPotion(true, 0x898180, "petrified");

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        IForgeRegistry<Potion> registry = event.getRegistry();
        for (Field field : DaemonicaPotions.class.getDeclaredFields()) {
            try {
                Object object = field.get(null);
                if (!(object instanceof Potion)) continue;
                registry.register((Potion) object);
            } catch (Exception e) {}
        }
    }

}
