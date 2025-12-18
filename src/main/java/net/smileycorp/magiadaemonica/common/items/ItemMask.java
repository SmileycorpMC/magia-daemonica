package net.smileycorp.magiadaemonica.common.items;

import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemMask extends ItemRelic {

    public ItemMask(String name) {
        super(name);
        BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(this, ItemArmor::dispenseArmor);
    }

    @Override
    public EntityEquipmentSlot getEquipmentSlot(ItemStack stack) {
        return EntityEquipmentSlot.HEAD;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        ItemStack current = player.getItemStackFromSlot(EntityEquipmentSlot.HEAD);
        player.setItemStackToSlot(EntityEquipmentSlot.HEAD, stack.copy());

        if (!current.isEmpty()) {
            player.setHeldItem(hand, current);
            return new ActionResult<>(EnumActionResult.SUCCESS, current);
        }
        stack.setCount(0);
        return new ActionResult<>(EnumActionResult.FAIL, stack);
    }

}
