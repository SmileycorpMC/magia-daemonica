package net.smileycorp.magiadaemonica.common.potions;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.magiadaemonica.common.Constants;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class DaemonicaPotions {

    public static final Potion TREMOR = new DaemonicaPotion(true, 0xD55E3C, "tremor");

    @SubscribeEvent
    public static void registerPotions(RegistryEvent.Register<Potion> event) {
        IForgeRegistry<Potion> registry = event.getRegistry();
        registry.register(TREMOR);
    }

}
