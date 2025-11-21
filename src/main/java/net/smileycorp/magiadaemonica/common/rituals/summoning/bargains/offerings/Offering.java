package net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.offerings;

import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.BargainUtils;

public interface Offering {

    void grant(EntityPlayer player);

    static Offering item(ItemStack stack) {
        return player -> {
            if (player.addItemStackToInventory(stack)) return;;
            EntityItem item = player.dropItem(stack, false);
            if (item == null) return;
            item.setNoPickupDelay();
            item.setOwner(player.getName());
        };
    }

    static Offering attributeModifier(IAttribute attribute, double value) {
        return player -> BargainUtils.addBonusAttribute(player, attribute, value);
    }

}
