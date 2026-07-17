package net.smileycorp.magiadaemonica.client.gui.widget;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.item.ItemStack;

public class ItemSlate extends Slate {

    public static final int WIDTH = 24, HEIGHT = 24;
    public static final int MOUSE_MARGIN = 2;

    private final ItemStack stack;

    public ItemSlate(GuiScreen parent, ItemStack stack, int x, int y, int animationTicks) {
        super(parent, x, y, 0, 130, WIDTH, HEIGHT, MOUSE_MARGIN, animationTicks);
        this.stack = stack;
    }

    @Override
    protected void renderDetails(boolean selected, boolean renderBack) {
        RenderItem renderer = mc.getRenderItem();
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, 0, -150);
        renderer.renderItemAndEffectIntoGUI(stack, -8, -8);
        GlStateManager.popMatrix();
    }

    public void renderTooltip(int mouseX, int mouseY) {
        GlStateManager.pushMatrix();
        //GlStateManager.translate(0, 0, 300);
        parent.renderToolTip(stack, /*parent.width / 4,  parent.height / 4 * 3*/ mouseX, mouseY);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
    }

    public ItemStack getStack() {
        return stack;
    }

}
