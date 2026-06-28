package net.smileycorp.magiadaemonica.common.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.config.ItemsConfig;

public class ItemDaemonicaFood extends ItemDaemonicaEdible {

    public ItemDaemonicaFood() {
        super("food", 1, 0.6f);
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

    @Override
    public int getHealAmount(ItemStack stack) {
        return Variant.get(stack.getMetadata()).getHunger();
    }

    @Override
    public float getSaturationModifier(ItemStack stack) {
        return Variant.get(stack.getMetadata()).getSaturation();
    }

    @Override
    public boolean canEat(ItemStack stack) {
        if (stack.getMetadata() == Variant.BARK.ordinal()) return ItemsConfig.oakBarkEdible;
        return super.canEat(stack);
    }

    public enum Variant {
        SUET("suet", ItemsConfig.suetHunger, ItemsConfig.suetSaturation),
        TALLOW("tallow", ItemsConfig.tallowHunger, ItemsConfig.tallowSaturation),
        BARK("bark", ItemsConfig.oakBarkHunger, ItemsConfig.oakBarkSaturation),
        PUMPKIN_SLICE("pumpkin_slice", ItemsConfig.pumpkinSliceHunger, ItemsConfig.pumpkinSliceSaturation);

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
            return meta < values().length ? values()[meta] : SUET;
        }

    }

}