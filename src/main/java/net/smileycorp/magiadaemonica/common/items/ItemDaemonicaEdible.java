package net.smileycorp.magiadaemonica.common.items;

import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.MagiaDaemonica;

public class ItemDaemonicaEdible extends ItemFood implements IMetaItem {

    protected final String name;
    protected final float saturation;

    public ItemDaemonicaEdible(String name, int hunger, float saturation) {
        super(hunger, 0, false);
        setRegistryName(Constants.loc(name));
        setUnlocalizedName(Constants.name(name));
        setCreativeTab(MagiaDaemonica.CREATIVE_TAB);
        this.name = name;
        this.saturation = saturation;
    }

    @Override
    public float getSaturationModifier(ItemStack stack) {
        return saturation;
    }

}
