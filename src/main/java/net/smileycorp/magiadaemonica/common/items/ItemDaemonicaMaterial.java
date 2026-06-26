package net.smileycorp.magiadaemonica.common.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.items.relics.InfernalRelic;

import javax.annotation.Nullable;
import java.util.List;

public class ItemDaemonicaMaterial extends ItemDaemonica {

    public ItemDaemonicaMaterial() {
        super("material");
        setHasSubtypes(true);
    }

    @Override
    public String byMeta(int meta) {
        if (meta == 666) return "infernal_scrap";
        return Variant.get(meta).getName();
    }

    @Override
    public int getMaxMeta() {
        return Variant.values().length;
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        for (int i = 0; i < Variant.values().length; i++) items.add(new ItemStack(this, 1, i));
        items.add(new ItemStack(this, 1, 666));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        if (stack.getMetadata() == 666) tooltip.add(new TextComponentTranslation("item.magiadaemonica.infernal_scrap.tooltip").getFormattedText());
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        return stack.getMetadata() == 666 ? InfernalRelic.RARITY : super.getForgeRarity(stack);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "item." + Constants.name(byMeta(stack.getMetadata()));
    }

    public enum Variant {
        FRANKINCENSE("frankincense"),
        OAK_ASH("oak_ash");

        private final String name;

        Variant(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }


        public static Variant get(int meta) {
            return meta < values().length ? values()[meta] : FRANKINCENSE;
        }

    }

}
