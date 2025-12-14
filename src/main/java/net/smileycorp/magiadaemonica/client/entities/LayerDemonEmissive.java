package net.smileycorp.magiadaemonica.client.entities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.entities.EntityAbstractDemon;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;

public class LayerDemonEmissive extends GeoLayerRenderer<EntityAbstractDemon> {

    public LayerDemonEmissive(RenderDemon renderer) {
        super(renderer);
    }

    @Override
    public void render(EntityAbstractDemon demon,  float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, Color colour) {
        Minecraft.getMinecraft().getRenderManager().renderEngine.bindTexture(getTextureLocation(demon));
        GeoModelProvider<EntityAbstractDemon> model = entityRenderer.getGeoModelProvider();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(!demon.isInvisible());
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 61680, 0);
        GlStateManager.enableLighting();
        Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
        entityRenderer.render(model.getModel(model.getModelLocation(demon)), demon, partialTicks, 1, 1, 1, 1);
        Minecraft.getMinecraft().entityRenderer.setupFogColor(false);
        GlStateManager.depthMask(true);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
    }

    public ResourceLocation getTextureLocation(EntityAbstractDemon demon) {
        ResourceLocation loc = entityRenderer.getGeoModelProvider().getTextureLocation(demon);
        return new ResourceLocation(loc.getResourceDomain(), loc.getResourcePath().replace(".png", "_eyes.png"));
    }

    @Override
    public boolean shouldCombineTextures() {
        return false;
    }

}
