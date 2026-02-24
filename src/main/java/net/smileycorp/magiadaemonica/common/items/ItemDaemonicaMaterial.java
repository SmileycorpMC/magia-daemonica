package net.smileycorp.magiadaemonica.common.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.smileycorp.magiadaemonica.common.Constants;

public class ItemDaemonicaMaterial extends ItemDaemonica {

    public ItemDaemonicaMaterial() {
        super("material");
        setHasSubtypes(true);
    }

    @Override
    public String byMeta(int meta) {
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
