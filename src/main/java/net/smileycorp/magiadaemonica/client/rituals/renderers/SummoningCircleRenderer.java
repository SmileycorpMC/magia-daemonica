package net.smileycorp.magiadaemonica.client.rituals.renderers;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.blocks.BlockChalkLine;
import net.smileycorp.magiadaemonica.common.blocks.BlockScentedCandle;
import net.smileycorp.magiadaemonica.common.blocks.DaemonicaBlocks;
import net.smileycorp.magiadaemonica.common.rituals.summoning.SummoningCircle;

import java.nio.FloatBuffer;

public class SummoningCircleRenderer implements RitualRenderer<SummoningCircle> {

    private static final ResourceLocation SUMMONING_RUNES = Constants.loc("textures/summoning_circles/summoning_runes.png");
    private static final ResourceLocation INFERNUM_SKY = Constants.loc("textures/misc/infernum_sky.png");
    private static final String LAVA = "minecraft:blocks/lava_still";
    private static final FloatBuffer MODELVIEW = GLAllocation.createDirectFloatBuffer(16);
    private static final FloatBuffer PROJECTION = GLAllocation.createDirectFloatBuffer(16);
    private final FloatBuffer buffer = GLAllocation.createDirectFloatBuffer(16);

    @Override
    public void render(SummoningCircle ritual, float partialTicks) {
        ResourceLocation name = ritual.getName();
        if (name == null) return;
        Minecraft mc = Minecraft.getMinecraft();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        TextureManager textureManager = mc.getTextureManager();
        boolean hasLighting = ritual.getTicksActive() <= 200;
        float w = ritual.getWidth() * 0.5f;
        float h = ritual.getHeight() * 0.5f;
        //setup lighting
        if (hasLighting) {
            RenderHelper.enableStandardItemLighting();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.enableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        } else {
            RenderHelper.disableStandardItemLighting();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.disableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        }
        GlStateManager.enableFog();
        //summoning circle
        float r;
        float g;
        float b;
        if (!hasLighting) {
            r = 1;
            g = 0.231f;
            b = 0;
        } else {
            float power = Math.min(ritual.getLastTickPower() + (ritual.getPower() - ritual.getLastTickPower()) * partialTicks, 2000) / 2000f;
            r = 1 - power * 0.35f;
            g = 1 - power;
            b = g;
            int i = mc.world.getCombinedLight(ritual.getCenterPos(), 0);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i % 65536, i / 65536);
        }
        GlStateManager.color(1f, 1f, 1f, 1f);
        textureManager.bindTexture(new ResourceLocation(name.getResourceDomain(), "textures/summoning_circles/" + name.getResourcePath() + ".png"));
        renderPlane(-w, 0.01, -h, w, 0.01, h, r, g, b, 1, false);
        //runes
        if (!hasLighting) {
            GlStateManager.enableBlend();
            textureManager.bindTexture(SUMMONING_RUNES);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            float t = ritual.getTicksActive() + partialTicks;
            GlStateManager.pushMatrix();
            GlStateManager.rotate(MathHelper.wrapDegrees(t * 18), 0, 1, 0);
            renderPlane(-w - 0.25, 0.5, -h -0.25, w + 0.25, 4, h + 0.25,  0.678f, 0, 0, 0.5f, true);
            GlStateManager.popMatrix();
            GlStateManager.pushMatrix();
            GlStateManager.rotate(MathHelper.wrapDegrees(-t * 20), 0, 1, 0);
            renderPlane(-w - 1, 0.5, -h -1, w + 1, 4, h + 1,  0.678f, 0, 0, 0.5f, true);
            GlStateManager.popMatrix();
            GlStateManager.disableBlend();
        }
        if (hasLighting) {
            RenderHelper.disableStandardItemLighting();
            GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
            GlStateManager.disableTexture2D();
            GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
        }
        //cracks
        if (ritual.getTicksActive() >= 320) {
            ResourceLocation crack = Constants.loc("textures/summoning_circles/crack_" +
                    MathHelper.clamp((ritual.getTicksActive() - 320) / 40, 0, 6)  + ".png");
            GlStateManager.enableBlend();
            textureManager.bindTexture(crack);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            renderPlane(-w, 0.02, -h, w, 0.01, h, 1f, 1f, 1f, 1f, false);
            GlStateManager.disableBlend();
        }
        if (ritual.getTicksActive() >= 520) {
            GlStateManager.disableFog();
            Minecraft.getMinecraft().entityRenderer.setupFogColor(true);
            GlStateManager.getFloat(2982, MODELVIEW);
            GlStateManager.getFloat(2983, PROJECTION);
            textureManager.bindTexture(INFERNUM_SKY);
            GlStateManager.texGen(GlStateManager.TexGen.S, 9216);
            GlStateManager.texGen(GlStateManager.TexGen.T, 9216);
            GlStateManager.texGen(GlStateManager.TexGen.R, 9216);
            GlStateManager.texGen(GlStateManager.TexGen.S, 9474, getBuffer(1, 0, 0, 0));
            GlStateManager.texGen(GlStateManager.TexGen.T, 9474, getBuffer(0, 1, 0, 0));
            GlStateManager.texGen(GlStateManager.TexGen.R, 9474, getBuffer(0, 0, 1, 0));
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.S);
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.T);
            GlStateManager.enableTexGenCoord(GlStateManager.TexGen.R);
            GlStateManager.matrixMode(5890);
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.pushMatrix();
            GlStateManager.loadIdentity();
            GlStateManager.translate(0.5, 0.5, 0);
            GlStateManager.scale(0.5, 0.5, 1);
            GlStateManager.translate(17, 2.334f * ((float)Minecraft.getSystemTime() % 800000f / 800000f), 0);
            GlStateManager.rotate(18, 0, 0, 1);
            GlStateManager.pushMatrix();
            GlStateManager.rotate(MathHelper.wrapDegrees(Minecraft.getSystemTime() * 3), 0, 1, 0);
            GlStateManager.scale(4.25, 1.0625, 1);
            GlStateManager.multMatrix(PROJECTION);
            GlStateManager.multMatrix(MODELVIEW);
            buffer.begin(7, DefaultVertexFormats.POSITION);
            //crack5
            if (ritual.getTicksActive() > 560) {
                //quad1
                buffer.pos(-1, 0.03, -0.125).endVertex();
                buffer.pos(-1.125, 0.03, 0.125).endVertex();
                buffer.pos(-1.0625, 0.03, 0.5625).endVertex();
                buffer.pos(-0.6875, 0.03, 1.25).endVertex();
                //quad2
                buffer.pos(-1, 0.03, -0.125).endVertex();
                buffer.pos(-0.6875, 0.03, 1.25).endVertex();
                buffer.pos(-0.25, 0.03, 1.125).endVertex();
                buffer.pos(-0.6875, 0.03, -0.437).endVertex();
                //quad3
                buffer.pos(-0.6875, 0.03, -0.437).endVertex();
                buffer.pos(-0.25, 0.03, 1.125).endVertex();
                buffer.pos(-0.125, 0.03, 0.875).endVertex();
                buffer.pos(-0.25, 0.03, -0.125).endVertex();
                //quad4
                buffer.pos(-0.75, 0.03, -0.75).endVertex();
                buffer.pos(-0.75, 0.03, -0.1875).endVertex();
                buffer.pos(0.75, 0.03, 0.4375).endVertex();
                buffer.pos(-0.5, 0.03, -0.8125).endVertex();
                //quad5
                buffer.pos(-0.25, 0.03, 0.75).endVertex();
                buffer.pos(0.75, 0.03, 0.75).endVertex();
                buffer.pos(0.75, 0.03, 0.4375).endVertex();
                buffer.pos(-0.25, 0.03, -0.125).endVertex();
                //quad6
                buffer.pos(0.4375, 0.03, 0.75).endVertex();
                buffer.pos(0.4375, 0.03, 1).endVertex();
                buffer.pos(0.75, 0.03, 1.0625).endVertex();
                buffer.pos(0.75, 0.03, 0.75).endVertex();
                //quad7
                buffer.pos(0.75, 0.03, 0.4375).endVertex();
                buffer.pos(1.5, 0.03, -0.125).endVertex();
                buffer.pos(1, 0.03, -0.125).endVertex();
                buffer.pos(-0.5, 0.03, -0.8125).endVertex();
                //quad8
                buffer.pos(-0.25, 0.03, -0.6775).endVertex();
                buffer.pos(0.875, 0.03, -0.125).endVertex();
                buffer.pos(0.875, 0.03, -0.5).endVertex();
                buffer.pos(1.125, 0.03, -0.875).endVertex();
            } else {
                //quad1
                buffer.pos(-0.437, 0.03, 0.1875).endVertex();
                buffer.pos(-0.25, 0.03, 0.437).endVertex();
                buffer.pos(0.437, 0.03, 0.437).endVertex();
                buffer.pos(0.0625, 0.03, -0.125).endVertex();
                //quad2
                buffer.pos(0.125, 0.03, -0.25).endVertex();
                buffer.pos(0.0625, 0.03, -0.125).endVertex();
                buffer.pos(0.437, 0.03, 0.0625).endVertex();
                buffer.pos(0.5625, 0.03, -0.437).endVertex();
                //quad3
                buffer.pos(0.437, 0.03, 0.1875).endVertex();
                buffer.pos(0.437, 0.03, 0.437).endVertex();
                buffer.pos(0.8125, 0.03, 0.75).endVertex();
                buffer.pos(0.5625, 0.03, 0.25).endVertex();
                //center quad
                buffer.pos(0.0625, 0.03, -0.125).endVertex();
                buffer.pos(0.437, 0.03, 0.437).endVertex();
                buffer.pos(0.5625, 0.03, 0.25).endVertex();
                buffer.pos(0.437, 0.03, 0.0625).endVertex();
            }
            tessellator.draw();
            GlStateManager.enableFog();
            GlStateManager.popMatrix();
            GlStateManager.matrixMode(5888);
            GlStateManager.disableTexGenCoord(GlStateManager.TexGen.S);
            GlStateManager.disableTexGenCoord(GlStateManager.TexGen.T);
            GlStateManager.disableTexGenCoord(GlStateManager.TexGen.R);
            GlStateManager.disableBlend();
            mc.entityRenderer.setupFogColor(false);
        }
        //candles
        WorldClient world = mc.world;
        BlockRendererDispatcher dispatcher = mc.getBlockRendererDispatcher();
        GlStateManager.disableLighting();
        textureManager.bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
        buffer.begin(7, DefaultVertexFormats.BLOCK);
        for (float[] candle : ritual.getCandles()) {
            float[] rotated = ritual.getRotation().apply(candle);
            BlockPos pos = new BlockPos(ritual.getCenter().addVector(rotated[0], 0, rotated[1]));
            IBlockState state = world.getBlockState(pos);
            boolean lit = false;
            if (state.getBlock() == DaemonicaBlocks.CHALK_LINE)
                lit = state.getValue(BlockChalkLine.CANDLE) == BlockChalkLine.Candle.LIT;
            IBlockState renderState = DaemonicaBlocks.SCENTED_CANDLE.getDefaultState().withProperty(BlockScentedCandle.LIT, lit);
            buffer.setTranslation(candle[0] - pos.getX() - 0.5f, -pos.getY(), candle[1] - pos.getZ() - 0.5f);
            dispatcher.getBlockModelRenderer().renderModel(world, dispatcher.getModelForState(renderState), renderState, pos, buffer, false, MathHelper.getPositionRandom(pos));
        }
        buffer.setTranslation(0, 0, 0);
        tessellator.draw();
        GlStateManager.disableFog();
        GlStateManager.enableLighting();
    }

    private FloatBuffer getBuffer(float p_147525_1_, float p_147525_2_, float p_147525_3_, float p_147525_4_) {
        this.buffer.clear();
        this.buffer.put(p_147525_1_).put(p_147525_2_).put(p_147525_3_).put(p_147525_4_);
        this.buffer.flip();
        return this.buffer;
    }

}
