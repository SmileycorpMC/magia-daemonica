package net.smileycorp.magiadaemonica.common.blocks;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.block.BlockBase;
import net.smileycorp.atlas.api.block.BlockSlabBase;
import net.smileycorp.atlas.api.block.BlockStairsBase;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.MagiaDaemonica;
import net.smileycorp.magiadaemonica.common.blocks.tiles.TileRitualBasic;
import net.smileycorp.magiadaemonica.common.blocks.tiles.TileScrollshelf;
import net.smileycorp.magiadaemonica.common.items.DaemonicaItems;
import net.smileycorp.magiadaemonica.config.BlocksConfig;

import java.lang.reflect.Field;
import java.util.List;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class DaemonicaBlocks {

    public static final List<Block> BLOCKS = Lists.newArrayList();

    //world
    //chalk
    public static final BlockBase CHALK = new BlockBase("chalk", Constants.MODID, Material.ROCK, SoundType.STONE, BlocksConfig.chalk.getHardness(), BlocksConfig.chalk.getResistance(), BlocksConfig.chalk.getHarvestLevel(), MagiaDaemonica.CREATIVE_TAB);
    public static final BlockStairsBase CHALK_STAIRS = new BlockStairsBase(CHALK);
    public static final BlockSlabBase CHALK_SLAB = new BlockSlabBase(CHALK, BlocksConfig.chalk.getHardness(), BlocksConfig.chalk.getResistance(), "pickaxe", BlocksConfig.chalk.getHarvestLevel(), false);
    public static final BlockSlabBase CHALK_DOUBLE_SLAB = new BlockSlabBase(CHALK, BlocksConfig.chalk.getHardness(), BlocksConfig.chalk.getResistance(), "pickaxe", BlocksConfig.chalk.getHarvestLevel(), true);

    //plants
    public static final BlockDaemonicaFlower FLOWER = new BlockDaemonicaFlower();
    public static final BlockDaemonicaCrop SPEARMINT = BlockDaemonicaCrop.create("spearmint", 4, true).setDrop(BlockDaemonicaCrop.DropFunction.SELF)
            .setSeed(BlockDaemonicaCrop.DropFunction.seed(new ItemStack(DaemonicaItems.SEEDS, 1, 0)));
    public static final BlockDaemonicaCrop WATERMINT = BlockDaemonicaCrop.create("watermint", 4, true).setDrop(BlockDaemonicaCrop.DropFunction.SELF)
            .setSeed(BlockDaemonicaCrop.DropFunction.seed(new ItemStack(DaemonicaItems.SEEDS, 1, 1)));
    public static final BlockDaemonicaCrop PEPPERMINT = BlockDaemonicaCrop.create("peppermint", 4, true).setDrop(BlockDaemonicaCrop.DropFunction.SELF);

    //ritual
    public static final BlockScentedCandle SCENTED_CANDLE = new BlockScentedCandle();
    public static final BlockChalkLine CHALK_LINE = new BlockChalkLine();
    public static final BlockChalkAshLine CHALK_ASH_LINE = new BlockChalkAshLine();
    public static final BlockChalkCandle CHALK_CANDLE = new BlockChalkCandle();

    //decorations
    public static final BlockScrollshelf SCROLLSHELF = new BlockScrollshelf();
    public static final BlockPetals ROSE_PETALS = new BlockPetals("rose");
    public static final BlockPetals LILAC_PETALS = new BlockPetals("lilac");
    public static final BlockPetals LAVENDER_PETALS = new BlockPetals("lavender");

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        for (Field field : DaemonicaBlocks.class.getDeclaredFields()) {
            try {
                Object object = field.get(null);
                if (!(object instanceof Block) || object == null) continue;
                register(registry, (Block) object);
            } catch (Exception e) {}
        }
        GameRegistry.registerTileEntity(TileRitualBasic.class, Constants.loc("ritual"));
        GameRegistry.registerTileEntity(TileScrollshelf.class, Constants.loc("scrollshelf"));
    }

    private static <T extends Block> void register(IForgeRegistry<Block> registry, T block) {
        registry.register(block);
        if (block instanceof BlockLine) return;
        BLOCKS.add(block);
    }

}
