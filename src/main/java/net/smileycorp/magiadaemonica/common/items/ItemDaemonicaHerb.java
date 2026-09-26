package net.smileycorp.magiadaemonica.common.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.config.ItemsConfig;

public class ItemDaemonicaHerb extends ItemDaemonicaEdible {

    public ItemDaemonicaHerb() {
        super("herb", 1, 0.6f);
        setHasSubtypes(true);
        setAlwaysEdible();
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

    @Override
    public int getHealAmount(ItemStack stack) {
        return Variant.get(stack.getMetadata()).getHunger();
    }

    @Override
    public float getSaturationModifier(ItemStack stack) {
        return Variant.get(stack.getMetadata()).getSaturation();
    }

    public enum Variant {
        SPEARMINT_LEAF("spearmint_leaf", ItemsConfig.spearmintLeafHunger, ItemsConfig.spearmintLeafSaturation),
        WATERMINT_LEAF("watermint_leaf", ItemsConfig.watermintLeafHunger, ItemsConfig.watermintLeafSaturation),
        PEPPERMINT_LEAF("peppermint_leaf", ItemsConfig.peppermintLeafHunger, ItemsConfig.peppermintLeafSaturation),
        OAK_BARK("oak_bark", ItemsConfig.oakBarkHunger, ItemsConfig.oakBarkSaturation),
        BIRCH_BARK("birch_bark", ItemsConfig.birchBarkHunger, ItemsConfig.birchBarkSaturation);

        private final String name;
        private final int hunger;
        private final float saturation;

        Variant(String name, int hunger, float saturation) {
            this.name = name;
            this.hunger = hunger;
            this.saturation = saturation;
        }

        public String getName() {
            return name;
        }

        public int getHunger() {
            return hunger;
        }

        public float getSaturation() {
            return saturation;
        }

        public static Variant get(int meta) {
            return meta < values().length ? values()[meta] : Variant.SPEARMINT_LEAF;
        }

    }

}
