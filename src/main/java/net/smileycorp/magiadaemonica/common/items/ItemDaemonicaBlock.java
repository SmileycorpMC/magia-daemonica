package net.smileycorp.magiadaemonica.common.items;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.smileycorp.atlas.api.block.BlockProperties;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.magiadaemonica.common.Constants;

public class ItemDaemonicaBlock<T extends Block & BlockProperties> extends ItemBlock implements IMetaItem {

    public ItemDaemonicaBlock(T block) {
        super(block);
        setRegistryName(block.getRegistryName());
        setUnlocalizedName(block.getUnlocalizedName().substring(0, 5));
        if (block.getMaxMeta() > 1) setHasSubtypes(true);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        for (int i = 0; i < getMaxMeta(); i++) items.add(new ItemStack(this, 1, i));
    }

    @Override
    public int getMaxMeta() {
        return ((BlockProperties)block).getMaxMeta();
    }
    
    @Override
    public String byMeta(int meta) {
        return ((BlockProperties)block).byMeta(meta);
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return getMaxMeta() > 0 ? "tile." + Constants.name(byMeta(stack.getMetadata())) : super.getUnlocalizedName();
    }
    
    
}
