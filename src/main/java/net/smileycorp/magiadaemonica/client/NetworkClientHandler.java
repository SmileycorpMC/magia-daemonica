package net.smileycorp.magiadaemonica.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.smileycorp.magiadaemonica.client.gui.GUIContract;
import net.smileycorp.magiadaemonica.client.particle.ParticleFullbrightPixel;
import net.smileycorp.magiadaemonica.client.particle.ParticlePixel;
import net.smileycorp.magiadaemonica.common.EnumParticle;
import net.smileycorp.magiadaemonica.common.capabilities.DaemonicaCapabilities;
import net.smileycorp.magiadaemonica.common.demons.contracts.Contract;
import net.smileycorp.magiadaemonica.common.entities.EntityContract;

public class NetworkClientHandler {

    public static void setSoul(float soul) {
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        if (player == null) return;
        if (!player.hasCapability(DaemonicaCapabilities.SOUL, null)) return;
        player.getCapability(DaemonicaCapabilities.SOUL, null).setSoul(soul);
    }

    public static void openContractGUI(int id, Contract contract) {
        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.world.getEntityByID(id);
        if (!(entity instanceof EntityContract)) return;
        mc.displayGuiScreen(new GUIContract((EntityContract)entity, contract));
    }

    public static void validateContract(int id) {
        Minecraft mc = Minecraft.getMinecraft();
        Entity entity = mc.world.getEntityByID(id);
        if (!(entity instanceof EntityContract)) return;
        if (mc.currentScreen instanceof GUIContract) ((GUIContract)mc.currentScreen).validate(id);
    }

    public static void spawnParticle(EnumParticle type, double x, double y, double z, Double... data) {
        Minecraft mc = Minecraft.getMinecraft();
        switch (type) {
            case PIXEL:
                mc.effectRenderer.addEffect(new ParticlePixel(mc.world, x, y, z, (int)(double)data[0], (int)(double)data[1], data[2], data[3], data[4], data[5]));
                break;
            case PIXEL_FULLBRIGHT:
                mc.effectRenderer.addEffect(new ParticleFullbrightPixel(mc.world, x, y, z, (int)(double)data[0], (int)(double)data[1], data[2], data[3], data[4], data[5]));
                break;
        }
    }

}
