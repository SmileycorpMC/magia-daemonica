package net.smileycorp.magiadaemonica.client.gui;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.IRarity;
import net.smileycorp.magiadaemonica.client.gui.widget.ItemSlate;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.network.AddItemMessage;

import java.io.IOException;
import java.util.List;

public class GuiChooseItem extends GuiChoice {

    private static final String TITLE = "title." + Constants.MODID + ".choose.item";
    private static final int SPACING = 4;

    private final List<List<ItemStack>> stacks;
    private final List<ItemSlate> slates = Lists.newArrayList();
    private TextFormatting colour;

    public GuiChooseItem(List<ItemStack> stacks) {
        super();
        int max = stacks.size() < 5 ? stacks.size() : Math.min((int) Math.ceil((float)stacks.size() / 2f), 7);
        boolean odd = stacks.size() % 2 == 1;
        int rows = (int) Math.ceil((float)stacks.size() / (float) max);
        while (rows >= max) max += 2;
        rows = (int) Math.ceil((float)stacks.size() / (float) max);
        int currentMax = stacks.size() >= 5 && odd && rows % 2 == 1 ? max : max -1;
        this.stacks = Lists.newArrayList();
        int list = 0;
        List<ItemStack> current = null;
        System.out.println(currentMax + ", " + rows + ", " + max);
        for (ItemStack stack : stacks) {
            if (this.stacks.size() < list + 1) {
                current = Lists.newArrayList();
                this.stacks.add(current);
                System.out.println(!odd + ", " + (rows < 3));
                if (!odd && rows < 3) continue;
                if (currentMax == max) currentMax--;
                else currentMax++;
            }
            current.add(stack);
            if (current.size() >= currentMax) list++;
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        if (stacks.isEmpty()) {
            closeGui();
            return;
        }
        boolean create = slates.isEmpty();
        IRarity highest = null;
        int y = (height - ((ItemSlate.HEIGHT + SPACING) * Math.min(stacks.size(), 6))) / 2;
        if (stacks.size() > 6) y += 4;
        int position = -1;
        for (int i = 0; i < stacks.size(); i++) {
            List<ItemStack> stacks = this.stacks.get(i);
            int x = (width - ((ItemSlate.WIDTH + SPACING) * stacks.size())) / 2;
            for (int j = 0; j < stacks.size(); j++) {
                position++;
                if (!create) {
                    slates.get(position).moveTo(x + i * (ItemSlate.WIDTH + SPACING), y);
                    continue;
                }
                ItemStack stack = stacks.get(j);
                if (stack == null) continue;
                IRarity rarity = stack.getItem().getForgeRarity(stack);
                if (highest == null) highest = rarity;
                else if (rarity instanceof EnumRarity) {
                    if (highest instanceof EnumRarity && ((EnumRarity) rarity).ordinal() > ((EnumRarity) highest).ordinal()) highest = rarity;
                } else if (highest instanceof EnumRarity) highest = rarity;
                int animTicks = position * 2;
                animTicks /= (1 + (position / 18));
                slates.add(new ItemSlate(this, stack, x + j * (ItemSlate.WIDTH + SPACING), y + i * (ItemSlate.HEIGHT + SPACING), animTicks + 10));
            }
        }
        if (create) colour = highest.getColor();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        slates.forEach(ItemSlate::tick);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        GlStateManager.pushMatrix();
        GlStateManager.scale(2, 2, 1);
        FontRenderer font = mc.fontRenderer;
        String text = colour + new TextComponentTranslation(TITLE).getFormattedText();
        GlStateManager.translate(0, 0, 1);
        font.drawString(text, (width / 4 - font.getStringWidth(text) / 2), 15, 0);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.popMatrix();
        slates.forEach(slate -> slate.render(mouseX, mouseY, partialTicks));
        for (ItemSlate slate : slates) if (slate.isActive() && slate.mouseOver(mouseX, mouseY)) slate.renderTooltip(mouseX, mouseY);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {}

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (ItemSlate slate : slates) if (mouseButton == 0 && slate.isActive() && slate.mouseOver(mouseX, mouseY)) {
            AddItemMessage.send(slate.getStack());
            closeGui();
            return;
        }
    }

}
