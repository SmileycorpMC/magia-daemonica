package net.smileycorp.magiadaemonica.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.magiadaemonica.common.Constants;

public abstract class Slate extends Gui {

    private static final ResourceLocation SLATE_TEXTURE = Constants.loc("textures/gui/slate.png");

    protected final Minecraft mc = Minecraft.getMinecraft();
    protected final GuiScreen parent;
    protected int x, y;
    protected final int width, height;
    protected final int u, v;
    protected final int dx, dy;
    protected final int mouseMargin;

    protected int animationTicks;

    public Slate(GuiScreen parent, int x, int y, int u, int v, int width, int height, int mouseMargin, int animationTicks) {
        this.parent = parent;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.u = u;
        this.v = v;
        dx = width / 2;
        dy = height / 2;
        this.mouseMargin = mouseMargin;
        this.animationTicks = animationTicks;
    }

    public boolean isActive() {
        return animationTicks <= 0;
    }

    public boolean mouseOver(int mouseX, int mouseY) {
        return mouseX >= x - mouseMargin && mouseX <= x + width + mouseMargin && mouseY >= y - mouseMargin && mouseY <= y + height + mouseMargin;
    }

    public void render(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + dx, y + dy, 100);
        boolean selected = false;
        boolean renderBack = false;
        //gui open animation
        if (!isActive()) {
            float ticks = animationTicks + partialTicks;
            GlStateManager.translate(ticks / 10f * -parent.width / 4f, ticks / 10f * parent.height / 2f, 0);
            GlStateManager.rotate(-ticks * 60f, 0, 1, 0);
            float scale = Math.min(4f / ticks, 1);
            GlStateManager.scale(scale, scale, scale);
            renderBack = true;
        }
        //rotate slate to face mouse
        else if (mouseOver(mouseX, mouseY)) {
            float xDis = x + (float) width / 2f - mouseX;
            float yDis = y + (float) height / 2f - mouseY;
            GlStateManager.rotate(xDis / (float) width * 30f, 0, -1, 0);
            GlStateManager.rotate(yDis / (float) height * 30f, 1, 1, 0);
            selected = true;
        }
        mc.getTextureManager().bindTexture(getTexture());
        //slate
        if (renderBack) {
            drawRect(-dx, -dy, -6, u + width, v, width - 2, height - 2, true);
            drawRect(-dx, -dy, -3, u, v, width, height, true);
        }
        drawRect(-dx, -dy, -3, u, v, width, height, false);
        drawRect(-dx, -dy, 0, u + width, v, width - 2, height - 2, false);
        renderDetails(selected, renderBack);
        GlStateManager.color(1, 1, 1);
        GlStateManager.popMatrix();
    }

    protected ResourceLocation getTexture() {
        return SLATE_TEXTURE;
    }

    protected abstract void renderDetails(boolean selected, boolean renderBack);

    public void tick() {
        if (animationTicks > 0) animationTicks--;
    }

    public void moveTo(int x, int y) {
        this.x = x;
        this.y = y;
    }

    protected void drawRect(int x, int y, float z, int u, int v, int width, int height, boolean backFace) {
        drawRect(x, y, z, u, v, width, height, 256, 256, backFace);
    }

    protected void drawRect(int x, int y, float z, int u, int v, int width, int height, int textureWidth, int textureHeight, boolean backFace) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        float u1 = (float) u / (float) textureWidth;
        float u2 = (float) (u + width) / (float) textureWidth;
        float v1 = (float) v / (float) textureHeight;
        float v2 = (float) (v + height) / (float) textureHeight;
        if (backFace) {
            buffer.pos(x + width, y, z).tex(u2, v1).normal(0, 1, 0).endVertex();
            buffer.pos(x + width, y + height, z).tex(u2, v2).normal(0, 1, 0).endVertex();
            buffer.pos(x, y + height, z).tex(u1, v2).normal(0, 1, 0).endVertex();
            buffer.pos(x, y, z).tex(u1, v1).normal(0, 1, 0).endVertex();
        } else {
            buffer.pos(x, y, z).tex(u1, v1).normal(0, 1, 0).endVertex();
            buffer.pos(x, y + height, z).tex(u1, v2).normal(0, 1, 0).endVertex();
            buffer.pos(x + width, y + height, z).tex(u2, v2).normal(0, 1, 0).endVertex();
            buffer.pos(x + width, y, z).tex(u2, v1).normal(0, 1, 0).endVertex();
        }
        tessellator.draw();
    }

}
