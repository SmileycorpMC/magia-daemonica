package net.smileycorp.magiadaemonica.client;

import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.smileycorp.magiadaemonica.client.rituals.RitualClientHandler;

public class ClientEventHandler {

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        RitualClientHandler.renderRituals();
    }

    @SubscribeEvent
    public void clientTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        RitualClientHandler.clientTick();
    }

    @SubscribeEvent
    public void logOut(PlayerEvent.PlayerLoggedOutEvent event) {
        RitualClientHandler.clear();
    }

}
