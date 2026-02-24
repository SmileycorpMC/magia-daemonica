package net.smileycorp.magiadaemonica.common.items;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreIngredient;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.atlas.api.block.BlockSlabBase;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.blocks.DaemonicaBlocks;

import java.lang.reflect.Field;
import java.util.List;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class DaemonicaItems {

    public static final List<Item> ITEMS = Lists.newArrayList();

    public static final ItemDaemonicaFood FOOD = new ItemDaemonicaFood();
    public static final ItemDaemonicaMaterial MATERIAL = new ItemDaemonicaMaterial();

    //tools
    public static final ItemChalkStick CHALK_STICK = new ItemChalkStick();
    public static final ItemKnife FLINT_KNIFE = new ItemKnife("flint_knife", 15, 1, () -> new OreIngredient("flint"));
    public static final ItemKnife BONE_KNIFE = new ItemKnife("bone_knife", 34, 2, () -> new OreIngredient("bone"));
    public static final ItemKnife LAPIS_KNIFE = new ItemKnife("lapis_knife", 106, 2, () -> new OreIngredient("gemLapis"));
    public static final ItemKnife PRISMARINE_KNIFE = new ItemKnife("prismarine_knife", 305, 3, () -> new OreIngredient("gemPrismarine"));
    public static final ItemKnife OBSIDIAN_KNIFE = new ItemKnife("obsidian_knife", 1064, 3, () -> new OreIngredient("obsidian"));

    //artifacts
    public static final ItemSicaInfernalem SICA_INFERNALEM = new ItemSicaInfernalem();
    public static final ItemPeccatumPrimordiale PECCATUM_PRIMORDIALE = new ItemPeccatumPrimordiale();
    public static final ItemCretaAeterna CRETA_AETERNA = new ItemCretaAeterna();
    public static final ItemCalixPerpetuus CALIX_PERPETUUS = new ItemCalixPerpetuus();
    public static final ItemInfernalMask MORS_LARVA = new ItemInfernalMask("mors_larva");
    public static final ItemInfernalMask FACIES_EXCORIATA = new ItemFaciesExcoriata();
    public static final ItemInfernalMask OCULUS_AETHEREUS = new ItemInfernalMask("oculus_aethereus");

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> registry = event.getRegistry();
        for (Block block : DaemonicaBlocks.BLOCKS) {
            if (block instanceof BlockProperties) if (!((BlockProperties)block).usesCustomItemHandler()) register(registry, new ItemDaemonicaBlock(block));
            if (block instanceof BlockStairs) {
                Item item = new ItemBlock(block);
                item.setRegistryName(block.getRegistryName());
                item.setUnlocalizedName(block.getUnlocalizedName());
                register(registry, item);
            }
        }
        registerSlab(registry, DaemonicaBlocks.CHALK_SLAB, DaemonicaBlocks.CHALK_DOUBLE_SLAB);
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

    private static void registerSlab(IForgeRegistry<Item> registry, BlockSlabBase half, BlockSlabBase full) {
        Item item = new ItemSlab(half, half, full);
        item.setRegistryName(half.getRegistryName());
        item.setUnlocalizedName(half.getUnlocalizedName());
        registry.register(item);
    }

}
