package net.smileycorp.magiadaemonica.client.gui.widget;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.smileycorp.magiadaemonica.common.capabilities.Boons;
import net.smileycorp.magiadaemonica.common.capabilities.Curses;
import net.smileycorp.magiadaemonica.common.demons.contracts.BoonRegistry;
import net.smileycorp.magiadaemonica.common.demons.contracts.CursesRegistry;

import java.awt.*;
import java.util.List;

public class BoonCurseSlate extends Slate {

    public static final int WIDTH = 80;
    public static final int HEIGHT = 112;
    private static final int MOUSE_MARGIN = 6;
    public static final int GEM_WIDTH = 10;

    private final ResourceLocation loc, texture;
    private final String name, description;
    private final boolean isCurse;
    private final Color colour, selectedColour;
    private final int maxLevel, gemX;
    private final float gemSpacing;
    private final int gemU;


    public BoonCurseSlate(GuiScreen parent, ResourceLocation loc, int x, int y, int animationTicks, boolean isCurse) {
        super(parent, x, y, 0, 0, WIDTH, HEIGHT, MOUSE_MARGIN, animationTicks);
        this.loc = loc;
        this.isCurse = isCurse;
        texture = new ResourceLocation(loc.getResourceDomain(), "textures/" + (isCurse ? "curses" : "boons") + "/" + loc.getResourcePath() + ".png");
        name = (isCurse ? "curse" : "boon") + "." + loc.getResourceDomain() + "." + loc.getResourcePath();
        description = name + ".description";
        colour = new Color(isCurse ? 0xFF94130E : 0xFF057F60);
        selectedColour = new Color(isCurse ? 0xFFCE3614 : 0xFF359E98);
        maxLevel = isCurse ? CursesRegistry.getMaxLevel(loc) : BoonRegistry.getMaxLevel(loc);
        gemSpacing = 30f / (float) maxLevel;
        gemX = (int) (-(GEM_WIDTH + gemSpacing) * maxLevel / 2f) - 3;
        gemU = isCurse ? 0 : 6;
    }

    public ResourceLocation getLoc() {
        return loc;
    }

    @Override
    protected void renderDetails(boolean selected, boolean renderBack) {
        Color colour = selected ? selectedColour : this.colour;
        //gems
        int gemU = this.gemU;
        if (selected) gemU += 12;
        int level = isCurse ? Curses.getLevel(mc.player, loc) : Boons.getLevel(mc.player, loc);
        for (int i = 0; i < maxLevel; i++) {
            int x = maxLevel == 1 ? -3 : (int) ((i + 0.5f) * (GEM_WIDTH + gemSpacing) + gemX);
            if (i + 1 > level) {
                drawRect(x, 98 - dy, 0.1f, 24, 112, 6, 9, false);
                continue;
            }
            drawRect(x, 98 - dy, 1, gemU, 112, 6, 9, false);
            drawRect(x + 2, 100 - dy, 2, gemU + 2, 123, 2, 5, false);
        }

        //icon
        mc.getTextureManager().bindTexture(texture);
        GlStateManager.color((float) colour.getRed() / 255f, (float) colour.getGreen() / 255f, (float) colour.getBlue() / 255f);
        drawRect(8 - dx, 16 - dy, 0.1f, 0, 0, 64, 64, 64, 64, false);

        //title
        FontRenderer font = mc.fontRenderer;
        String text = new TextComponentTranslation(this.name).getFormattedText();
        GlStateManager.translate(0, 0, 0.5f);
        font.drawString(text, -font.getStringWidth(text) / 2, 6 - dy, colour.getRGB());

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
            font.drawString(str, -font.getStringWidth(str) / 2, 108 - dy + i * font.FONT_HEIGHT, colour.getRGB());
        }
    }

}
