package net.smileycorp.magiadaemonica.client.entities;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.smileycorp.magiadaemonica.client.entities.model.ModelDemon;
import net.smileycorp.magiadaemonica.common.entities.EntityAbstractDemon;
import software.bernie.geckolib3.core.util.Color;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderDemon extends GeoEntityRenderer<EntityAbstractDemon> {

    public RenderDemon(RenderManager rm) {
        super(rm, new ModelDemon());
        addLayer(new LayerDemonEmissive(this));
    }

    @Override
    public Color getRenderColor(EntityAbstractDemon demon, float partialTicks) {
        float brightness = demon.getBrightness();
        return Color.ofRGBA(brightness, brightness, brightness, demon.getAlpha()) ;
    }

    @Override
    public void render(GeoModel model, EntityAbstractDemon demon, float partialTicks, float red, float green, float blue, float alpha) {
        GlStateManager.pushMatrix();
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        super.render(model, demon, partialTicks, red, green, blue, alpha);
        GlStateManager.disableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.popMatrix();
    }

}
