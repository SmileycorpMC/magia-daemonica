package net.smileycorp.magiadaemonica.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.GuiScreen;

import java.util.List;

public class GuiChoice extends GuiScreen {

    protected final List<GuiChoice> queuedScreens = Lists.newArrayList();

    public void queueScreen(GuiChoice gui) {
        queuedScreens.add(gui);
    }

    protected void closeGui() {
        if (queuedScreens.isEmpty()) {
            mc.displayGuiScreen(null);
            return;
        }
        GuiChoice screen = queuedScreens.get(0);
        mc.displayGuiScreen(screen);
        for (int i = 1; i < queuedScreens.size(); i++) screen.queueScreen(queuedScreens.get(i));
    }

}
