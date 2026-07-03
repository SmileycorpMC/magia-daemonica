package net.smileycorp.magiadaemonica.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.network.InscribeScrollMessage;
import net.smileycorp.magiadaemonica.common.network.PacketHandler;

import java.io.IOException;
import java.util.List;

public class GuiBlankScroll extends GuiScreen {

    private static final int WIDTH = 234, HEIGHT = 142;
    private static final ResourceLocation TEXTURE = Constants.loc("textures/gui/blank_scroll.png");

    private final boolean mainhand;
    private final List<String> text = Lists.newArrayList();
    private String str = "";
    private int cursorFlashTimer = 0;

    public GuiBlankScroll(boolean mainhand) {
        this.mainhand = mainhand;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        int x = (width - WIDTH) / 2;
        int y = (height - HEIGHT) / 2;
        mc.getTextureManager().bindTexture(TEXTURE);
        drawTexturedModalRect(x, y, 0, 0, WIDTH, HEIGHT);
        if (cursorFlashTimer++ == 40) cursorFlashTimer = -40;
        if (text.isEmpty() && cursorFlashTimer > 0) {
            mc.fontRenderer.drawString("_", x + WIDTH / 2, y + HEIGHT / 2, 0, false);
            return;
        }
        int textY = (HEIGHT - 9 * (text.size() - 1)) / 2;
        for (int i = 0; i < text.size(); i++) {
            String str = text.get(i);
            int textWidth = mc.fontRenderer.getStringWidth(str);
            if (cursorFlashTimer > 0 && i == text.size() - 1) str += "_";
            mc.fontRenderer.drawString(str, x + (WIDTH - textWidth) / 2, y + textY + i * 9, 0, false);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            mc.displayGuiScreen(null);
            return;
        }
        if (keyCode == 28) {
            PacketHandler.NETWORK_INSTANCE.sendToServer(new InscribeScrollMessage(str, mainhand));
            mc.displayGuiScreen(null);
            return;
        }
        if (GuiScreen.isKeyComboCtrlV(keyCode)) str += GuiScreen.getClipboardString();
        if (keyCode == 14 && !str.isEmpty()) str = str.substring(0, str.length() - 1);
        if (ChatAllowedCharacters.isAllowedCharacter(typedChar) && str.length() <= 255) str += typedChar;
        refreshLines();
    }

    public void refreshLines() {
        text.clear();
        int position = 0;
        while (position < str.length()) {
            int size = Math.min(40, str.length() - position);
            while (mc.fontRenderer.getStringWidth(str.substring(position, position + size)) > 130) size--;
            int newPos = position + size;
            if (str.substring(position, newPos).contains("\n")) {
                int i = str.substring(position, newPos).indexOf("\n");
                text.add(str.substring(position, position + i));
                position = position + i + 1;
                continue;
            }
            if (newPos >= str.length()) {
                text.add(str.substring(position));
                break;
            }
            for (int i = 0; i <= size; i++) {
                if (i == size) {
                    text.add(str.substring(position, newPos + 1));
                    position = newPos;
                    break;
                } else if (str.charAt(newPos - i) == ' ') {
                    text.add(str.substring(position, newPos - i + 1));
                    position = newPos - i + 1;
                    break;
                }
            }
        }
    }

}
