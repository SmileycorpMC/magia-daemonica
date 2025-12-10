package net.smileycorp.magiadaemonica.common.blocks;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;
import net.smileycorp.atlas.api.block.BlockBase;
import net.smileycorp.atlas.api.block.ShapedBlock;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.MagiaDaemonica;

import java.lang.reflect.Field;
import java.util.List;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class DaemonicaBlocks {

    public static final List<Block> BLOCKS = Lists.newArrayList();

    public static final ShapedBlock CHALK = new ShapedBlock("chalk", Constants.MODID, Material.ROCK, SoundType.STONE, 0.75f, 0.75f, 0, MagiaDaemonica.CREATIVE_TAB);
    public static final BlockDaemonicaFlower FLOWER = new BlockDaemonicaFlower();
    public static final BlockScentedCandle SCENTED_CANDLE = new BlockScentedCandle();
    public static final BlockChalkLine CHALK_LINE = new BlockChalkLine();
    public static final BlockBase CHALK_ASH_LINE = new BlockChalkAshLine();

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event) {
        IForgeRegistry<Block> registry = event.getRegistry();
        CHALK.registerBlocks(registry);
        for (Field field : DaemonicaBlocks.class.getDeclaredFields()) {
            try {
                Object object = field.get(null);
                if (!(object instanceof Block) || object == null) continue;
                register(registry, (Block) object);
            } catch (Exception e) {}
        }
    }

    private static <T extends Block> void register(IForgeRegistry<Block> registry, T block) {
        registry.register(block);
        if (block instanceof BlockLine) return;
        BLOCKS.add(block);
    }

}
