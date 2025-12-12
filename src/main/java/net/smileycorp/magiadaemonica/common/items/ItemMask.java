package net.smileycorp.magiadaemonica.common.items;

import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ItemMask extends ItemRelic {

    public ItemMask(String name) {
        super(name);
    }

    @Nullable
    @Override
    public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EntityEquipmentSlot.HEAD;
    }

}
