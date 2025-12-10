package net.smileycorp.magiadaemonica.common.items;

import net.minecraft.item.Item;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.MagiaDaemonica;

public class ItemDaemonica extends Item implements IMetaItem {

    protected final String name;

    public ItemDaemonica(String name) {
        setRegistryName(Constants.loc(name));
        setUnlocalizedName(Constants.name(name));
        setCreativeTab(MagiaDaemonica.CREATIVE_TAB);
        this.name = name;
    }
    
}
