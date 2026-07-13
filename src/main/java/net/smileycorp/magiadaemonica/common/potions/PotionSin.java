package net.smileycorp.magiadaemonica.common.potions;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.EnumParticle;

import java.util.Random;

public class PotionSin extends DaemonicaPotion {

    public static final ResourceLocation LOCK_TEXTURE = Constants.loc("textures/gui/sin_lock.png");

    public static boolean REMOVABLE = false;

    protected PotionSin() {
        super(true, 0, "sin");
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return true;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        Random rand = entity.getRNG();
        if (entity.isInvisible()) return;
        if (rand.nextBoolean()) return;
        int c = rand.nextInt(50);
        EnumParticle.PIXEL.send(entity.dimension, entity.posX + (rand.nextFloat() - 0.5) * (double)entity.width * 2,
                entity.posY + rand.nextFloat() * (double)entity.height, entity.posZ + (rand.nextDouble() - 0.5) * (double)entity.width * 2,
                (double) (c + (c << 8) + (c << 16)), 40d, 0d, 0.01d, 0d, 0.25d);
    }

    @Override
    public boolean hasCustomParticles() {
        return true;
    }

    @Override
    public int getGuiSortColor(PotionEffect effect) {
        return Integer.MAX_VALUE;
    }

    @SideOnly(Side.CLIENT)
    protected void renderEffect(PotionEffect effect, int x, int y, float alpha) {
        GlStateManager.pushMatrix();
        float t = (float) Math.sin(effect.getDuration() * 0.02) + 1;
        GlStateManager.color(1 - (0.125f * t), 0.25f - (0.125f * t), 0.25f - (0.125f * t), alpha);
        Minecraft.getMinecraft().renderEngine.bindTexture(getTexture(effect));
        Gui.drawScaledCustomSizeModalRect(x, y, 0, 0 , 18, 18, 18, 18, 18, 18);
        GlStateManager.popMatrix();
    }

    public static void remove(EntityLivingBase entitylivingbase) {
        REMOVABLE = true;
        entitylivingbase.removePotionEffect(DaemonicaPotions.SIN);
        REMOVABLE = false;
    }

}
