package net.smileycorp.magiadaemonica.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.magiadaemonica.common.Constants;

public class PhoenicianFontRenderer extends FontRenderer {

    private static PhoenicianFontRenderer INSTANCE;
    private static final ResourceLocation TEXTURE = Constants.loc("textures/font/phoenician.png");
    private static final ResourceLocation VANILLA_TEXTURE = new ResourceLocation("textures/font/ascii.png");
    private final FontRenderer vanillaFont;
    private final char TETH = '\u00e7';

    public PhoenicianFontRenderer(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode) {
        super(gameSettingsIn, location, textureManagerIn, unicode);
        vanillaFont = Minecraft.getMinecraft().fontRenderer;
    }

    @Override
    protected void renderStringAtPos(String text, boolean shadow) {
        super.renderStringAtPos(text.replace("th", String.valueOf(TETH)), shadow);
    }

    @Override
    public int getStringWidth(String text) {
        return super.getStringWidth(text.replace("th", String.valueOf(TETH)));
    }

    @Override
    public int getCharWidth(char c) {
        if (c == TETH || (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')) return super.getCharWidth(c);
        return vanillaFont.getCharWidth(c);
    }

    @Override
    public float renderDefaultChar(int ch, boolean italic) {
        boolean phoenician = ch == 135 || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z');
        int i = ch % 16 * 8;
        int j = ch / 16 * 8;
        int k = italic ? 1 : 0;
        bindTexture(phoenician ? locationFontTexture : VANILLA_TEXTURE);
        int l = phoenician ? charWidth[ch] : vanillaFont.charWidth[ch];
        float f = (float)l - 0.01F;
        GlStateManager.glBegin(5);
        GlStateManager.glTexCoord2f((float)i / 128.0F, (float)j / 128.0F);
        GlStateManager.glVertex3f(this.posX + (float)k, this.posY, 0.0F);
        GlStateManager.glTexCoord2f((float)i / 128.0F, ((float)j + 7.99F) / 128.0F);
        GlStateManager.glVertex3f(this.posX - (float)k, this.posY + 7.99F, 0.0F);
        GlStateManager.glTexCoord2f(((float)i + f - 1.0F) / 128.0F, (float)j / 128.0F);
        GlStateManager.glVertex3f(this.posX + f - 1.0F + (float)k, this.posY, 0.0F);
        GlStateManager.glTexCoord2f(((float)i + f - 1.0F) / 128.0F, ((float)j + 7.99F) / 128.0F);
        GlStateManager.glVertex3f(this.posX + f - 1.0F - (float)k, this.posY + 7.99F, 0.0F);
        GlStateManager.glEnd();
        return (float)l;
    }

    public static PhoenicianFontRenderer getInstance() {
        if (INSTANCE == null) {
            Minecraft mc = Minecraft.getMinecraft();
            INSTANCE = new PhoenicianFontRenderer(mc.gameSettings, TEXTURE, mc.renderEngine, false);
        }
        return INSTANCE;
    }

}
