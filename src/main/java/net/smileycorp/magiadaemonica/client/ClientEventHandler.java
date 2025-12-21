package net.smileycorp.magiadaemonica.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.smileycorp.magiadaemonica.client.rituals.RitualsClient;
import net.smileycorp.magiadaemonica.common.items.DaemonicaItems;
import net.smileycorp.magiadaemonica.common.potions.DaemonicaPotions;
import net.smileycorp.magiadaemonica.common.rituals.Rituals;

public class ClientEventHandler {

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
        RitualsClient.getInstance().renderRituals();
    }

    @SubscribeEvent
    public void logOut(PlayerEvent.PlayerLoggedOutEvent event) {
        RitualsClient.getInstance().clear();
    }

    @SubscribeEvent
    public void tick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        Minecraft mc = Minecraft.getMinecraft();
        if (mc.isGamePaused()) return;
        WorldClient world = mc.world;
        if (world == null) return;
        Rituals.get(world).tick();
    }

    @SubscribeEvent
    public void setupCamera(EntityViewRenderEvent.CameraSetup event) {
        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.getRenderViewEntity();
        if (!(entity instanceof EntityLivingBase)) return;
        EntityLivingBase living = (EntityLivingBase) entity;
        if (living.isPotionActive(DaemonicaPotions.TREMOR)) {
            float a = (living.getActivePotionEffect(DaemonicaPotions.TREMOR).getAmplifier() + 4) * 0.25f;
            float t = (mc.getRenderPartialTicks() + living.ticksExisted) * a * 0.75f;
            event.setPitch((float) (event.getPitch() + a * Math.sin((2*t) + 3)));
            event.setYaw((float) (event.getYaw() + a * Math.cos(t)));
            event.setRoll((float) (event.getRoll() + a * Math.sin(5 - (t*3))));
        }
    }

    @SubscribeEvent
    public void fogDensity(EntityViewRenderEvent.FogDensity event) {
        Entity entity = Minecraft.getMinecraft().getRenderViewEntity();
        if (!(entity instanceof EntityLivingBase)) return;
        if (((EntityLivingBase) entity).getItemStackFromSlot(EntityEquipmentSlot.HEAD).getItem() != DaemonicaItems.OCULUS_AETHEREUS) return;
        event.setDensity(0);
        event.setCanceled(true);
    }

    /*@SubscribeEvent
    public void renderLiving$Pre(RenderLivingEvent.Pre<EntityLivingBase> event) {
        EntityLivingBase entity = event.getEntity();
        if (!entity.isPotionActive(DaemonicaPotions.PETRIFIED)) return;
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.color(1, 1, 1, 0.5f);
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }

    @SubscribeEvent
    public void renderLiving$Post(RenderLivingEvent.Post<EntityLivingBase> event) {
        EntityLivingBase entity = event.getEntity();
        if (!entity.isPotionActive(DaemonicaPotions.PETRIFIED)) return;
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.popMatrix();
    }*/

}
