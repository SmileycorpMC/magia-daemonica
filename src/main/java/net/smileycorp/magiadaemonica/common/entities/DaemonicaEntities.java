package net.smileycorp.magiadaemonica.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityEntryBuilder;
import net.minecraftforge.registries.DataSerializerEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.demons.Demon;

@Mod.EventBusSubscriber
public class DaemonicaEntities {

    private static int ID;

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityEntry> event) {
        IForgeRegistry<EntityEntry> registry = event.getRegistry();
        register(registry, "demonic_trader", EntityDemonicTrader.class, 80);
    }

    public static void register(IForgeRegistry<EntityEntry> registry, String name, Class<? extends Entity> clazz, int range) {
        registry.register(EntityEntryBuilder.create().entity(clazz).id(Constants.loc(name), ID++)
                .name(Constants.name(name)).tracker(range, 1, true).build());
    }

    @SubscribeEvent
    public static void registerDataSerializers(RegistryEvent.Register<DataSerializerEntry> event) {
        IForgeRegistry<DataSerializerEntry> registry = event.getRegistry();
        register(registry, "demon", Demon.SERIALIZER);
    }

    public static void register(IForgeRegistry<DataSerializerEntry> registry, String name, DataSerializer<?> serializer) {
        registry.register(new DataSerializerEntry(serializer).setRegistryName(Constants.loc(name)));
    }

}
