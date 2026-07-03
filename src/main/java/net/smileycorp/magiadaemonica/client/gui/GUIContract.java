package net.smileycorp.magiadaemonica.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.smileycorp.magiadaemonica.client.ModLocalization;
import net.smileycorp.magiadaemonica.client.PhoenicianFontRenderer;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.DaemonicaSoundEvents;
import net.smileycorp.magiadaemonica.common.demons.Domain;
import net.smileycorp.magiadaemonica.common.demons.contracts.Contract;
import net.smileycorp.magiadaemonica.common.entities.EntityContract;
import net.smileycorp.magiadaemonica.common.network.SignContractMessage;

import java.io.IOException;
import java.util.List;

public class GUIContract extends GuiScreen {

    private static final int WIDTH = 120, HEIGHT = 180;
    private static final int COLOUR = 0x7A0000, SHADOW = 0xB59B8D;
    private static final ResourceLocation TEXTURE = Constants.loc("textures/gui/contract.png");
    private static final PhoenicianFontRenderer PHOENICIAN = PhoenicianFontRenderer.getInstance();
    private static final ResourceLocation TEXT = Constants.loc("contract");
    private static final ResourceLocation FINEPRINT = Constants.loc("contract_fineprint");
    private static final int SIGN_WIDTH = 96;
    private static final int SIGN_HEIGHT = 11;
    private static final int SIGN_X = (WIDTH - SIGN_WIDTH) / 2;
    private static final int SIGN_Y = 130;

    private final EntityContract entity;
    private final List<String> text;
    private final List<String> fineprint;
    private int signTicks = -1;
    private boolean failed = false;

    public GUIContract(EntityContract entity, Contract contract) {
        Minecraft mc = Minecraft.getMinecraft();
        this.entity = entity;
        text = ModLocalization.INSTANCE.getText(TEXT, (WIDTH - 20) * 2, mc.player.getDisplayName().getFormattedText(),
                contract.getCostText(), entity.getDemon().getFormalName().getFormattedText(), contract.getOfferingText(), Domain.values().length);
        fineprint = ModLocalization.INSTANCE.getText(FINEPRINT, (WIDTH - 20) * 4);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        int x = (width - WIDTH) / 2;
        int y = (height - HEIGHT) / 2;
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(x, y, 0, 0, WIDTH, HEIGHT);
        String title = new TextComponentTranslation("contract.magiadaemonica.title").getFormattedText();
        int titleX = x + (WIDTH - PHOENICIAN.getStringWidth(title)) / 2;
        int titleY = y + 7;
        //vanilla shadow rendering is too dark and looks bad so we custom render it here
        PHOENICIAN.drawString(title, titleX + 1, titleY, SHADOW, false);
        PHOENICIAN.drawString(title, titleX, titleY + 1, SHADOW, false);
        PHOENICIAN.drawString(title, titleX + 1, titleY + 1, SHADOW, false);
        //actual title rendering
        PHOENICIAN.drawString(title, titleX, titleY, COLOUR, false);
        //signature box
        mc.getTextureManager().bindTexture(TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.color(1, 1, 1, 1);
        drawTexturedModalRect(x + SIGN_X, y + SIGN_Y, 0, 180, SIGN_WIDTH, SIGN_HEIGHT);
        //sign text
        if (failed) {
            drawTexturedModalRect(x + SIGN_X, y + SIGN_Y, 0, 202, SIGN_WIDTH, SIGN_HEIGHT);
            if (signTicks++ == 60) mc.displayGuiScreen(null);
        }
        else if (signTicks < 0 && mouseX >= x + SIGN_X && mouseY >= y + SIGN_Y &&
                mouseX <= x + SIGN_X + SIGN_WIDTH && mouseY <= y + SIGN_Y + SIGN_HEIGHT + 1) {
            drawTexturedModalRect(x + SIGN_X, y + SIGN_Y, 0, 191, SIGN_WIDTH, SIGN_HEIGHT);
            String sign = I18n.format("contract.magiadaemonica.sign");
            mc.fontRenderer.drawString(sign, x + SIGN_X + (SIGN_WIDTH - mc.fontRenderer.getStringWidth(sign)) / 2,
                    y + SIGN_Y + 1, COLOUR, false);
        }
        //signature animation
        else if (signTicks >= 0) {
            String name = mc.player.getDisplayName().getFormattedText();
            if (signTicks++/5 > name.length() + 20) {
                mc.displayGuiScreen(null);
            }
            if (signTicks > 5) mc.fontRenderer.drawString(signTicks/5 >= name.length() ? name :
                            name.substring(0, (signTicks/5) - 1),
                    x + SIGN_X + 1, y + SIGN_Y + 1, COLOUR, false);
        }
        //contract text
        GlStateManager.disableBlend();
        GlStateManager.pushMatrix();
        //half the size of the title text
        GlStateManager.scale(0.5, 0.5, 0.5);
        y = (y + 20) * 2;
        for (int i = 0; i < text.size(); i ++) {
            if (i > 0) y += 9;
            String string = text.get(i);
            mc.fontRenderer.drawString(string, 2*(x + 10),  y, COLOUR, false);
        }
        //fineprint
        //half the size of the contract body
        GlStateManager.pushMatrix();
        GlStateManager.scale(0.5, 0.5, 0.5);
        for (int i = 0; i < fineprint.size(); i ++) {
            String string = fineprint.get(i);
            mc.fontRenderer.drawString(string, 4*(x + 10),  y * 2 + 156 + i * 9, SHADOW, false);
        }
        GlStateManager.popMatrix();
        GlStateManager.popMatrix();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (failed || signTicks >= 0) return;
        int x = (width - WIDTH) / 2;
        int y = (height - HEIGHT) / 2;
        if (mouseX >= x + SIGN_X && mouseY >= y + SIGN_Y &&
                mouseX <= x + SIGN_X + SIGN_WIDTH && mouseY <= y + SIGN_Y + SIGN_HEIGHT + 1) {
            SignContractMessage.send(entity.getEntityId());
        }
    }

    public void validate(int id) {
        if (id == entity.getEntityId()) signTicks = 0;
        mc.player.playSound(DaemonicaSoundEvents.CONTRACT_SIGN, 0.75f, 1);
    }

}
