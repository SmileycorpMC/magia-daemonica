package net.smileycorp.magiadaemonica.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.Gui;
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
    private final StringBuffer str = new StringBuffer();
    private int cursor = 0;
    private int cursorX, cursorY = 0;
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
        int textY = (HEIGHT - fontRenderer.FONT_HEIGHT * (text.size() - 1)) / 2;
        boolean renderCursor = cursorFlashTimer / 6 % 2 == 0;
        if (text.isEmpty() && renderCursor) {
            mc.fontRenderer.drawString("_", x + WIDTH / 2, y + HEIGHT / 2, 0, false);
            return;
        }
        for (int i = 0; i < text.size(); i++) {
            String line = text.get(i);
            int textWidth = mc.fontRenderer.getStringWidth(line);
            mc.fontRenderer.drawString(line, x + (WIDTH - textWidth) / 2, y + textY + i * fontRenderer.FONT_HEIGHT, 0, false);
            if (i == cursorY && renderCursor) {
                int cursorRenderX = x + (WIDTH + textWidth) / 2;
                if (cursorX < line.length()) cursorRenderX -= fontRenderer.getStringWidth(line.substring(cursorX));
                int cursorRenderY = y + textY + cursorY * fontRenderer.FONT_HEIGHT;
                if (cursor == str.length()) mc.fontRenderer.drawString("_", cursorRenderX, cursorRenderY, 0, false);
                else Gui.drawRect(cursorRenderX, cursorRenderY - 1, cursorRenderX + 1, cursorRenderY + 1 + fontRenderer.FONT_HEIGHT, 0xFFA0A0A0);
            }
        }
    }

    @Override
    public void updateScreen() {
        cursorFlashTimer++;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        //escape
        if (keyCode == 1) {
            mc.displayGuiScreen(null);
            return;
        }
        //enter
        if (keyCode == 28) {
            PacketHandler.NETWORK_INSTANCE.sendToServer(new InscribeScrollMessage(str.toString(), mainhand));
            mc.displayGuiScreen(null);
            return;
        }
        //left arrow
        if (keyCode == 203) {
            if (cursor == 0) return;
            cursor--;
            refreshCursor();
            return;
        }
        //right arrow
        else if (keyCode == 205) {
            if (cursor >= str.length()) return;
            cursor++;
            refreshCursor();
            return;
        }
        //up arrow
        else if (keyCode == 200) {
            if (cursor == 0) return;
            if (cursorY == 0) cursor = 0;
            else cursor = getPositionInString((int)((float) cursorX / (float) text.get(cursorY).length() * (float) text.get(cursorY - 1).length()), cursorY - 1);
            refreshCursor();
            return;
        }
        //down arrow
        else if (keyCode == 208) {
            if (cursor == 0) return;
            if (cursorY == text.size() - 1) cursor = str.length();
            else cursor = getPositionInString((int)((float) cursorX / (float) text.get(cursorY).length() * (float) text.get(cursorY + 1).length()), cursorY + 1);
            refreshCursor();
            return;
        }
        //ctr + v
        else if (isKeyComboCtrlV(keyCode)) {
            String clipboard = ChatAllowedCharacters.filterAllowedCharacters(getClipboardString());
            clipboard = clipboard.substring(0, Math.min(255 - str.length(), clipboard.length() - 1));
            str.insert(cursor, clipboard);
            cursor += clipboard.length() - 1;
        }
        //backspace
        else if (keyCode == 14) {
            if (str.length() == 0 || cursor == 0) return;
            str.deleteCharAt(cursor - 1);
            cursor--;
        }
        //delete
        else if (keyCode == 211) {
             if (str.length() < 0 || cursor >= str.length()) return;
            str.deleteCharAt(cursor);
        }
        //normal text characters
        else if (ChatAllowedCharacters.isAllowedCharacter(typedChar) && str.length() <= 255) {
            str.insert(cursor, typedChar);
            cursor++;
        }
        refreshLines();
        refreshCursor();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (mouseButton != 0) return;
        int textY = (height - fontRenderer.FONT_HEIGHT * (text.size() - 1)) / 2;
        int y = (mouseY - textY) / fontRenderer.FONT_HEIGHT;
        if (y < 0 || y >= text.size()) return;
        String line = text.get(y);
        int textWidth = mc.fontRenderer.getStringWidth(line);
        int textX = (width - textWidth) / 2;
        int x = (mouseX - textX) / (textWidth / line.length());
        if (x < 0 || x >= line.length()) return;
        cursor = getPositionInString(x, y);
        refreshCursor();
    }

    public void refreshLines() {
        text.clear();
        int position = 0;
        while (position < str.length()) {
            int size = Math.min(40, str.length() - position);
            while (mc.fontRenderer.getStringWidth(str.substring(position, position + size)) > 130) size--;
            int newPos = position + size;
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

    public void refreshCursor() {
        cursorX = cursor;
        cursorY = 0;
        for (String str : text) {
            if (cursorX == str.length() && cursorY < text.size() - 1) {
                cursorY++;
                cursorX = 1;
                return;
            }
            if (cursorX <= str.length()) return;
            cursorY++;
            cursorX -= str.length() - 1;
        }
    }

    public int getPositionInString(int x, int y) {
        int position = 0;
        for (int i = 0; i < y; i++) position += text.get(i).length() -1;
        return position + x;
    }

}
