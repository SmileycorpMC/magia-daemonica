package net.smileycorp.magiadaemonica.common;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.smileycorp.magiadaemonica.common.capabilities.ISoul;
import net.smileycorp.magiadaemonica.common.command.CommandSoul;
import net.smileycorp.magiadaemonica.common.network.PacketHandler;

@Mod.EventBusSubscriber
public class CommonProxy {
	
	public void preInit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new MagicaDaemonicaEventHandler());
		PacketHandler.initPackets();
		CapabilityManager.INSTANCE.register(ISoul.class, new ISoul.Storage(), ISoul.Soul::new);
	}

	public void init(FMLInitializationEvent event) {

	}
	
	public void postInit(FMLPostInitializationEvent event) {

	}

	public void serverStart(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandSoul());
	}

}
