package net.smileycorp.magiadaemonica.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.client.resources.IReloadableResourceManager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.atlas.api.client.CustomStateMapper;
import net.smileycorp.atlas.api.client.MetaStateMapper;
import net.smileycorp.atlas.api.client.SlabStateMapper;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.magiadaemonica.client.entities.RenderContract;
import net.smileycorp.magiadaemonica.client.entities.RenderDemon;
import net.smileycorp.magiadaemonica.client.rituals.RitualsClient;
import net.smileycorp.magiadaemonica.client.rituals.renderers.SummoningCircleRenderer;
import net.smileycorp.magiadaemonica.common.CommonProxy;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.blocks.DaemonicaBlocks;
import net.smileycorp.magiadaemonica.common.entities.EntityContract;
import net.smileycorp.magiadaemonica.common.entities.EntityDemonicTrader;
import net.smileycorp.magiadaemonica.common.items.DaemonicaItems;
import net.smileycorp.magiadaemonica.common.items.ItemCalixPerpetuus;
import net.smileycorp.magiadaemonica.common.rituals.summoning.SummoningCircle;

@EventBusSubscriber(value = Side.CLIENT, modid= Constants.MODID)
public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
		RitualsClient.registerRitualRenderer(SummoningCircle.ID, new SummoningCircleRenderer());
		ModLocalization.INSTANCE.register(Constants.loc("contract"));
		ModLocalization.INSTANCE.register(Constants.loc("contract_fineprint"));
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		((IReloadableResourceManager)Minecraft.getMinecraft().getResourceManager()).registerReloadListener(ModLocalization.INSTANCE);
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		super.postInit(event);
	}
	
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event) {
		ModelLoader.setCustomStateMapper(DaemonicaBlocks.CHALK_SLAB, new SlabStateMapper(DaemonicaBlocks.CHALK_SLAB));
		ModelLoader.setCustomStateMapper(DaemonicaBlocks.CHALK_DOUBLE_SLAB, new CustomStateMapper(Constants.MODID, "chalk_slab", "double"));
		ModelLoader.setCustomStateMapper(DaemonicaBlocks.FLOWER, new MetaStateMapper());
		for (Item item : DaemonicaItems.ITEMS) if (item instanceof IMetaItem &! (item instanceof ItemBlock &&
				((ItemBlock)item).getBlock() instanceof BlockProperties &&
				((BlockProperties)((ItemBlock) item).getBlock()).usesCustomItemHandler())) {
			if (((IMetaItem) item).getMaxMeta() > 0) for (int i = 0; i < ((IMetaItem) item).getMaxMeta(); i++) {
				ModelResourceLocation loc = new ModelResourceLocation(Constants.locStr(((IMetaItem) item).byMeta(i)));
				ModelLoader.setCustomModelResourceLocation(item, i, loc);
			}
			else {
				ModelResourceLocation loc = new ModelResourceLocation(item.getRegistryName().toString());
				ModelLoader.setCustomModelResourceLocation(item, 0, loc);
			}
		}
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(DaemonicaBlocks.CHALK_SLAB), 0,
				new ModelResourceLocation(Constants.locStr("chalk_slab"), "normal"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(DaemonicaBlocks.CHALK_STAIRS), 0,
				new ModelResourceLocation(Constants.locStr("chalk_stairs"), "normal"));
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(DaemonicaBlocks.FLOWER), 0,
				new ModelResourceLocation(Constants.locStr("lavender"), "inventory"));
		RenderingRegistry.registerEntityRenderingHandler(EntityDemonicTrader.class, RenderDemon::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityContract.class, RenderContract::new);
	}

	@SubscribeEvent
	public static void itemColourHandler(ColorHandlerEvent.Item event) {
		ItemColors registry = event.getItemColors();
		registry.registerItemColorHandler(((stack, tintIndex) -> tintIndex > 0 && ItemCalixPerpetuus.hasPotion(stack)
				? PotionUtils.getColor(stack) : -1), DaemonicaItems.CALIX_PERPETUUS);
	}
	
}
