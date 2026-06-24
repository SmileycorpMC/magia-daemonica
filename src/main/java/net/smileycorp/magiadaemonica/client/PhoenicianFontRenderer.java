package net.smileycorp.magiadaemonica.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.magiadaemonica.common.Constants;

public class PhoenicianFontRenderer extends FontRenderer {

    private static PhoenicianFontRenderer INSTANCE;

    private final String TETH = "\u00a7";

    public PhoenicianFontRenderer(GameSettings gameSettingsIn, ResourceLocation location, TextureManager textureManagerIn, boolean unicode) {
        super(gameSettingsIn, location, textureManagerIn, unicode);
    }

    @Override
    protected void renderStringAtPos(String text, boolean shadow) {
        super.renderStringAtPos(text.replace("th", TETH), shadow);
    }

    @Override
    public int getStringWidth(String text) {
        return super.getStringWidth(text.replace("th", TETH));
    }

    public static PhoenicianFontRenderer getInstance() {
        if (INSTANCE == null) {
            Minecraft mc = Minecraft.getMinecraft();
            INSTANCE = new PhoenicianFontRenderer(mc.gameSettings, Constants.loc("textures/font/phoenician.png"), mc.renderEngine, false);
        }
        return INSTANCE;
    }

}
