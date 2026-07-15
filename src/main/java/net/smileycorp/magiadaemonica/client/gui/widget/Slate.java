package net.smileycorp.magiadaemonica.client.gui.widget;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.capabilities.Boons;
import net.smileycorp.magiadaemonica.common.capabilities.Curses;
import net.smileycorp.magiadaemonica.common.demons.contracts.BoonRegistry;
import net.smileycorp.magiadaemonica.common.demons.contracts.CursesRegistry;

import java.awt.*;
import java.util.List;

public class Slate {

    private static final ResourceLocation SLATE_TEXTURE = Constants.loc("textures/gui/slate.png");
    public static final int WIDTH = 80;
    public static final int HEIGHT = 112;
    private static final int DX = WIDTH / 2;
    private static final int DY = HEIGHT / 2;
    private static final int MOUSE_MARGIN = 6;
    public static final int GEM_WIDTH = 6;

    private final GuiScreen parent;
    private final ResourceLocation loc, texture;
    private final int x, y;
    private final String name, description;
    private final boolean isCurse;
    private final Color colour, selectedColour;
    private final int maxLevel, gemX;
    private final float gemSpacing;
    private final int gemU;

    private int animationTicks;

    public Slate(GuiScreen parent, ResourceLocation loc, int x, int y, int animationTicks, boolean isCurse) {
        this.parent = parent;
        this.loc = loc;
        this.x = x;
        this.y = y;
        this.isCurse = isCurse;
        texture = new ResourceLocation(loc.getResourceDomain(), "textures/" + (isCurse ? "curses" : "boons") + "/" + loc.getResourcePath() + ".png");
        name = (isCurse ? "curse" : "boon") + "." + loc.getResourceDomain() + "." + loc.getResourcePath();
        description = name + ".description";
        colour = new Color(isCurse ? 0xFF94130E : 0xFF057F60);
        //selectedColor = new Color(isCurse ? 0xFFA61E10 : 0xFF35A08B);
        selectedColour = new Color(isCurse ? 0xFFCE3614 : 0xFF359E98);
        maxLevel = isCurse ? CursesRegistry.getMaxLevel(loc) : BoonRegistry.getMaxLevel(loc);
        gemSpacing = 30f / (float) maxLevel;
        gemX = (int) (-(GEM_WIDTH + gemSpacing) * maxLevel / 2f) - 3;
        gemU = isCurse ? 0 : 6;
        this.animationTicks = animationTicks;
    }

    public boolean isActive() {
        return animationTicks <= 0;
    }

    public boolean mouseOver(int mouseX, int mouseY) {
        return mouseX >= x - MOUSE_MARGIN && mouseX <= x + WIDTH + MOUSE_MARGIN && mouseY >= y - MOUSE_MARGIN && mouseY <= y + HEIGHT + MOUSE_MARGIN;
    }

    public ResourceLocation getLoc() {
        return loc;
    }

    public void render(int mouseX, int mouseY, float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        GlStateManager.pushMatrix();
        GlStateManager.translate(x + WIDTH - DX, y + DY, 100);
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
            float xDis = x + (float) WIDTH / 2f - mouseX;
            float yDis = y + (float) HEIGHT / 2f - mouseY;
            GlStateManager.rotate(xDis * 0.43f, 0, -1, 0);
            GlStateManager.rotate(yDis * 0.25f, 1, 1, 0);
            //GlStateManager.rotate(15f, yDis / 4f, -xDis/ 4f, 0);
            selected = true;
        }
        Color colour = selected ? selectedColour : this.colour;
        mc.getTextureManager().bindTexture(SLATE_TEXTURE);
        //slate
        if (renderBack) {
            drawRect(-DX, -DY, -6, WIDTH, 0, WIDTH - 2, HEIGHT - 2, true);
            drawRect(-DX, -DY, -3, 0, 0, WIDTH, HEIGHT, true);
        }
        drawRect(-DX, -DY, -3, 0, 0, WIDTH, HEIGHT, false);
        //drawRect(1 - DX, 1 - DY, -2, WIDTH, 0, WIDTH - 4, HEIGHT - 4, false);
        drawRect(-DX, -DY, 0, WIDTH, 0, WIDTH - 2, HEIGHT - 2, false);

        //gems
        int gemU = this.gemU;
        if (selected) gemU += 12;
        int level = isCurse ? Curses.getLevel(mc.player, loc) : Boons.getLevel(mc.player, loc);
        for (int i = 0; i < maxLevel; i++) {
            int x = maxLevel == 1 ? -3 : (int) ((i + 0.5f) * (GEM_WIDTH + gemSpacing) + gemX);
            if (i + 1 > level) {
                drawRect(x, 98 - DY, 0.1f, 24, 112, 6, 9, false);
                continue;
            }
            drawRect(x, 98 - DY, 1, gemU, 112, 6, 9, false);
            drawRect(x + 2, 100 - DY, 2, gemU + 2, 123, 2, 5, false);
        }

        //icon
        mc.getTextureManager().bindTexture(texture);
        GlStateManager.color((float) colour.getRed() / 255f, (float) colour.getGreen() / 255f, (float) colour.getBlue() / 255f);
        drawRect(8 - DX, 16 - DY, 0.1f, 0, 0, 64, 64, 64, 64, false);

        //title
        FontRenderer font = mc.fontRenderer;
        String text = new TextComponentTranslation(this.name).getFormattedText();
        GlStateManager.translate(0, 0, 0.5f);
        font.drawString(text, -font.getStringWidth(text) / 2, 6 - DY, colour.getRGB());

        //description
        text = new TextComponentTranslation(this.description).getFormattedText();
        GlStateManager.scale(0.5, 0.5, 1);
        List<String> description = Lists.newArrayList();
        int position = 0;
        while (position < text.length()) {
            int size = Math.min(40, text.length() - position);
            while (mc.fontRenderer.getStringWidth(text.substring(position, position + size)) > 130) size--;
            int newPos = position + size;
            if (newPos >= text.length()) {
                description.add(text.substring(position));
                break;
            }
            for (int i = 0; i <= size; i++) {
                if (i == size) {
                    description.add(text.substring(position, newPos + 1));
                    position = newPos;
                    break;
                } else if (text.charAt(newPos - i) == ' ') {
                    description.add(text.substring(position, newPos - i + 1));
                    position = newPos - i + 1;
                    break;
                }
            }
        }
        for (int i = 0; i < description.size(); i++) {
            String str = description.get(i);
            font.drawString(str, -font.getStringWidth(str) / 2, 108 - DY + i * font.FONT_HEIGHT, colour.getRGB());
        }

        GlStateManager.color(1, 1, 1);
        GlStateManager.popMatrix();
    }
    
    public void tick() {
        if (animationTicks > 0) animationTicks--;
    }

    private void drawRect(int x, int y, float z, int u, int v, int width, int height, boolean backFace) {
        drawRect(x, y, z, u, v, width, height, 256, 256, backFace);
    }

    private void drawRect(int x, int y, float z, int u, int v, int width, int height, int textureWidth, int textureHeight, boolean backFace) {
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
