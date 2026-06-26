package net.smileycorp.magiadaemonica.common.items.relics;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.smileycorp.magiadaemonica.common.items.ItemMask;

import javax.annotation.Nullable;
import java.util.List;

public class ItemInfernalMask extends ItemMask implements InfernalRelic {

    public ItemInfernalMask(String name) {
        super(name);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        tooltip.add(new TextComponentTranslation("item.magiadaemonica.infernal_mask").getFormattedText());
        tooltip.add(new TextComponentTranslation("item.magiadaemonica." + name + ".tooltip").getFormattedText());
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        return RARITY;
    }

}
