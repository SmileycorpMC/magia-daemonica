package net.smileycorp.magiadaemonica.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.smileycorp.magiadaemonica.client.gui.widget.BoonCurseSlate;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.network.AddCurseBoonMessage;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class GuiChooseBoonCurse extends GuiChoice {

    private final List<BoonCurseSlate> slates = Lists.newArrayList();
    private final List<ResourceLocation> options;
    private final boolean isCurse;
    private final String title;
    private final Color colour;

    public GuiChooseBoonCurse(boolean isCurse, List<ResourceLocation> options) {
       this.options = options;
       this.isCurse = isCurse;
       title = "title." + Constants.MODID + ".choose." + ( isCurse ? "curse" : "boon");
       colour = new Color(isCurse ? 0xFF94130E : 0xFF057F60);
    }

    @Override
    public void initGui() {
        super.initGui();
        if (options.isEmpty()) {
            closeGui();
            return;
        }
        int slateSpacing = 60 / options.size();
        int x = options.size() == 1 ? (width - BoonCurseSlate.WIDTH) / 2 : (width - ((BoonCurseSlate.WIDTH + slateSpacing) * options.size())) / 2;
        int y = (height - BoonCurseSlate.HEIGHT) / 2;
        if (slates.isEmpty()) for (int i = 0; i < options.size(); i++) slates.add(new BoonCurseSlate(this, options.get(i), x + i * (BoonCurseSlate.WIDTH + slateSpacing), y, i * 2 + 10, isCurse));
        else for (int i = 0; i < slates.size(); i++) slates.get(i).moveTo(x + i * (BoonCurseSlate.WIDTH + slateSpacing), y);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        slates.forEach(BoonCurseSlate::tick);
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
        for (BoonCurseSlate slate : slates) if (mouseButton == 0 && slate.isActive() && slate.mouseOver(mouseX, mouseY)) {
            AddCurseBoonMessage.send(isCurse, slate.getLoc());
            closeGui();
            return;
        }
    }

}
