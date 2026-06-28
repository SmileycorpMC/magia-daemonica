package net.smileycorp.magiadaemonica.common.items.relics;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemLoricaAculeata extends ItemInfernalArmour {

    public ItemLoricaAculeata() {
        super("lorica_aculeata", EntityEquipmentSlot.CHEST, 0, 0, SoundEvents.ITEM_ARMOR_EQUIP_IRON);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        tooltip.add(new TextComponentTranslation("item.magiadaemonica.unequipable").getFormattedText());
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        return HashMultimap.create();
    }

}
