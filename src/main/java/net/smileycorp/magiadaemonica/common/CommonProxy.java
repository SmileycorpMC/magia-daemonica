package net.smileycorp.magiadaemonica.common;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.smileycorp.magiadaemonica.common.advancements.DaemonicaAdvancements;
import net.smileycorp.magiadaemonica.common.capabilities.*;
import net.smileycorp.magiadaemonica.common.command.CommandBoons;
import net.smileycorp.magiadaemonica.common.command.CommandCurses;
import net.smileycorp.magiadaemonica.common.command.CommandSoul;
import net.smileycorp.magiadaemonica.common.demons.contracts.ContractRegistry;
import net.smileycorp.magiadaemonica.common.invocations.InvocationsRegistry;
import net.smileycorp.magiadaemonica.common.network.PacketHandler;
import net.smileycorp.magiadaemonica.common.rituals.RitualsRegistry;
import net.smileycorp.magiadaemonica.common.world.DaemonicaWorldGen;
import net.smileycorp.magiadaemonica.config.BlocksConfig;
import net.smileycorp.magiadaemonica.config.ItemsConfig;
import net.smileycorp.magiadaemonica.config.WorldConfig;
import software.bernie.geckolib3.GeckoLib;

@Mod.EventBusSubscriber
public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
		//configs
		BlocksConfig.syncConfig(event);
		ItemsConfig.syncConfig(event);
		WorldConfig.syncConfig(event);

		//event handlers, worldgen and packets
		PacketHandler.initPackets();
		MinecraftForge.EVENT_BUS.register(new DaemonicaEventHandler());
		GameRegistry.registerWorldGenerator(new DaemonicaWorldGen(), 99);

		//capabilities
		CapabilityManager.INSTANCE.register(Soul.class, new Soul.Storage(), Soul.Impl::new);
		CapabilityManager.INSTANCE.register(Contracts.class, new Contracts.Storage(), Contracts.Impl::new);
		CapabilityManager.INSTANCE.register(Affiliation.class, new Affiliation.Storage(), Affiliation.Impl::new);
		CapabilityManager.INSTANCE.register(Curses.class, new Curses.Storage(), Curses.Impl::new);
		CapabilityManager.INSTANCE.register(Boons.class, new Boons.Storage(), Boons.Impl::new);

		//registries
		DaemonicaAdvancements.register();
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
		event.registerServerCommand(new CommandCurses());
		event.registerServerCommand(new CommandBoons());
	}

}
