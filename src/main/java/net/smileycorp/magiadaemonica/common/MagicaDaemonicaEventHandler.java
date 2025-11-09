package net.smileycorp.magiadaemonica.common;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.smileycorp.magiadaemonica.common.capabilities.ISoul;

public class MagicaDaemonicaEventHandler {

	@SubscribeEvent
	public void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event) {
		Entity entity = event.getObject();
		if (!(entity instanceof EntityPlayer)) return;
		if (!entity.hasCapability(MagiaDaemonicaCapabilities.SOUL, null)) return;
		event.addCapability(Constants.loc("soul"), new ISoul.Provider());
	}

	@SubscribeEvent
	public void respawn(PlayerEvent.Clone event) {
		World world;
		world.getWorldInfo().isHardcoreModeEnabled()
	}
	
}
