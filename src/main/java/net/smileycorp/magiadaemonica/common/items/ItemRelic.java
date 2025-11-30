package net.smileycorp.magiadaemonica.common.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;

import javax.annotation.Nullable;
import java.util.List;

public class ItemRelic extends ItemDaemonica {

    public static final IRarity RARITY = new IRarity() {
        @Override
        public TextFormatting getColor() {
            return TextFormatting.GOLD;
        }

        @Override
        public String getName() {
            return "Relic";
        }
    };

    public ItemRelic(String name) {
        super(name);
        setMaxStackSize(1);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        tooltip.add(new TextComponentTranslation("item.magicadaemonica." + name + ".tooltip").getFormattedText());
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        return RARITY;
    }
}
