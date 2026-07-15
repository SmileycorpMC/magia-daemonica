package net.smileycorp.magiadaemonica.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.smileycorp.magiadaemonica.client.gui.widget.Slate;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.network.AddCurseBoonMessage;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GUIPickBoonCurse extends GuiScreen {

    private final List<GUIPickBoonCurse> queuedScreens = Lists.newArrayList();
    private final List<Slate> slates = Lists.newArrayList();
    private final List<ResourceLocation> options;
    private final boolean isCurse;
    private final String title;
    private final Color colour;

    private int animationTicks = -1;

    public GUIPickBoonCurse(boolean isCurse, List<ResourceLocation> options) {
       this.options = options;
       this.isCurse = isCurse;
       title = "title." + Constants.MODID + ( isCurse ? ".curse" : ".boon") + ".choose";
       colour = new Color(isCurse ? 0xFF94130E : 0xFF057F60);
    }

    public void queueScreen(GUIPickBoonCurse gui) {
        queuedScreens.add(gui);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        if (animationTicks <= 0) return;
        animationTicks--;
        slates.forEach(Slate::tick);
    }

    @Override
    public void initGui() {
        super.initGui();
        if (options.isEmpty()) {
            mc.displayGuiScreen(null);
            return;
        }
        int slateSpacing = 60 / options.size();
        int x = (width - ((Slate.WIDTH + slateSpacing) * options.size())) / 2;
        int y = (height - Slate.HEIGHT) / 2;
        slates.clear();
        for (int i = 0; i < options.size(); i++) slates.add(new Slate(this, options.get(i), x + i * (Slate.WIDTH + slateSpacing), y, animationTicks == -1 ? i * 2 + 10 : 0, isCurse));
        if (animationTicks == -1) animationTicks = slates.size() * 2 + 10;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        slates.forEach(slate -> slate.render(mouseX, mouseY, partialTicks));
        GlStateManager.pushMatrix();
        GlStateManager.scale(2, 2, 1);
        FontRenderer font = mc.fontRenderer;
        String text = new TextComponentTranslation(title).getFormattedText();
        GlStateManager.translate(0, 0, 1);
        font.drawString(text, (width / 4 - font.getStringWidth(text) / 2), 15, colour.getRGB());
        GlStateManager.popMatrix();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {}

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (Slate slate : slates) if (mouseButton == 0 && slate.isActive() && slate.mouseOver(mouseX, mouseY)) {
            AddCurseBoonMessage.send(isCurse, slate.getLoc());
            if (queuedScreens.isEmpty()) {
                mc.displayGuiScreen(null);
                return;
            }
            GUIPickBoonCurse screen = queuedScreens.get(0);
            mc.displayGuiScreen(screen);
            for (int i = 1; i < queuedScreens.size(); i++) screen.queueScreen(queuedScreens.get(i));
            return;
        }
    }

}
