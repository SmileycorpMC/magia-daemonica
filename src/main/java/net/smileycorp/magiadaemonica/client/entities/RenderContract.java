package net.smileycorp.magiadaemonica.client.entities;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.entities.EntityContract;

import javax.annotation.Nullable;

public class RenderContract extends Render<EntityContract> {

    public static final ResourceLocation TEXTURE = Constants.loc("textures/entities/contract.png");

    public RenderContract(RenderManager rm) {
        super(rm);
    }

    @Override
    public void doRender(EntityContract entity, double x, double y, double z, float entityYaw, float partialTicks) {
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
        RenderManager rm = Minecraft.getMinecraft().getRenderManager();
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        rm.renderEngine.bindTexture(TEXTURE);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(180.0F - rm.playerViewY, 0, 1, 0);
        GlStateManager.rotate((float)(rm.options.thirdPersonView == 2 ? -1 : 1) * -rm.playerViewX, 1, 0, 0);
        GlStateManager.enableNormalize();
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
        final float height = Math.min(entity.ticksExisted * 0.015625f, 0.625f);
        float yOffset = 0;
        if (entity.ticksExisted > 40) yOffset += Math.sin((entity.ticksExisted - 40) * 0.1) * 0.05;
        GlStateManager.translate(-0.25f, yOffset, -0.25f);
        //contract
        buffer.pos(0, 0.5 - height, 0).tex(0, height * 1.6).endVertex();
        buffer.pos(0.5, 0.5 - height, 0).tex(1, height * 1.6).endVertex();
        buffer.pos(0.5, 0.5, 0).tex(1, 0).endVertex();
        buffer.pos(0, 0.5, 0).tex(0, 0).endVertex();
        tessellator.draw();
        //fire
        if (entity.ticksExisted < 40) {
            Minecraft mc = Minecraft.getMinecraft();
            Vec3d dir = DirectionUtils.getDirectionVecXZ(entity, mc.player);
            TextureAtlasSprite sprite = Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite("minecraft:blocks/fire_layer_0");
            mc.getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
            GlStateManager.pushMatrix();
            float v = sprite.getMinV() + (sprite.getMaxV() - sprite.getMinV()) * 0.5f;
            GlStateManager.translate(dir.x * 0.025, 0, dir.z * 0.025);
            buffer.begin(7, DefaultVertexFormats.POSITION_TEX);
            buffer.pos(0, 0.5 - height, 0).tex(sprite.getMinU(), v).endVertex();
            buffer.pos(0.5, 0.5 - height, 0).tex(sprite.getMaxU(), v).endVertex();
            buffer.pos(0.5, 0.75 - height, 0).tex(sprite.getMaxU(), sprite.getMinV()).endVertex();
            buffer.pos(0, 0.75 - height, 0).tex(sprite.getMinU(), sprite.getMinV()).endVertex();
            tessellator.draw();
            GlStateManager.popMatrix();
        }
        GlStateManager.disableBlend();
        GlStateManager.disableNormalize();
        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();
    }

    @Nullable
    @Override
    protected ResourceLocation getEntityTexture(EntityContract entity) {
        return TEXTURE;
    }

}
