package net.smileycorp.magiadaemonica.common.items;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.blocks.DaemonicaBlocks;

import java.lang.reflect.Field;
import java.util.List;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class DaemonicaItems {

    public static final List<Item> ITEMS = Lists.newArrayList();

    public static final ItemChalkStick CHALK_STICK = new ItemChalkStick();
    public static final ItemDaemonicaFood MATERIAL = new ItemDaemonicaFood();

    //artifacts
    public static final ItemSicaInfernalem SICA_INFERNALEM = new ItemSicaInfernalem();
    public static final ItemPeccatumPrimordiale PECCATUM_PRIMORDIALE = new ItemPeccatumPrimordiale();
    public static final ItemCretaAeterna CRETA_AETERNA = new ItemCretaAeterna();
    public static final ItemCalixPerpetuus CALIX_PERPETUUS = new ItemCalixPerpetuus();
    public static final ItemMask MORS_LARVA = new ItemMask("mors_larva");
    public static final ItemMask FACIES_EXCORIATA = new ItemMask("facies_excoriata");

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        for (Block block : DaemonicaBlocks.BLOCKS) if (block instanceof BlockProperties) register(registry, new ItemDaemonicaBlock(block));
        DaemonicaBlocks.CHALK.registerItems(registry);
        for (Field field : DaemonicaItems.class.getDeclaredFields()) {
            try {
                Object object = field.get(null);
                if (!(object instanceof Item) || object == null) continue;
                register(registry, (Item) object);
            } catch (Exception e) {}
        }
    }

    private static <T extends Item> void register(IForgeRegistry<Item> registry, T item) {
        registry.register(item);
        ITEMS.add(item);
    }

}
