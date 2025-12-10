package net.smileycorp.magiadaemonica.common;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.smileycorp.magiadaemonica.common.capabilities.Soul;
import net.smileycorp.magiadaemonica.common.command.CommandSoul;
import net.smileycorp.magiadaemonica.common.demons.contracts.ContractRegistry;
import net.smileycorp.magiadaemonica.common.invocations.InvocationsRegistry;
import net.smileycorp.magiadaemonica.common.network.PacketHandler;
import net.smileycorp.magiadaemonica.common.rituals.RitualsRegistry;
import net.smileycorp.magiadaemonica.common.world.DaemonicaWorldGen;
import net.smileycorp.magiadaemonica.config.WorldConfig;
import software.bernie.geckolib3.GeckoLib;

@Mod.EventBusSubscriber
public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
		WorldConfig.syncConfig(event);
		MinecraftForge.EVENT_BUS.register(new DaemonicaEventHandler());
		PacketHandler.initPackets();
		CapabilityManager.INSTANCE.register(Soul.class, new Soul.Storage(), Soul.Impl::new);
		GameRegistry.registerWorldGenerator(new DaemonicaWorldGen(), 99);
		RitualsRegistry.registerDefaults();
		ContractRegistry.registerDefaults();
		InvocationsRegistry.registerDefaults();
		GeckoLib.initialize();
	}

	public void init(FMLInitializationEvent event) {

	}
	
	public void postInit(FMLPostInitializationEvent event) {

	}

	public void serverStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandSoul());
	}

}
