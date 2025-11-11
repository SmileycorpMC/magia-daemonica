package net.smileycorp.magiadaemonica.common;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class PriceUtils {

    private static final UUID ATTRIBUTE_MODIFIER = UUID.fromString("cdc852af-afe8-4eac-85bc-3f90e670a8da");

    public static void addAttribute(EntityPlayer player, IAttribute attribute, double value) {
        IAttributeInstance attributes = player.getEntityAttribute(attribute);
        AttributeModifier modifier = attributes.getModifier(ATTRIBUTE_MODIFIER);
        if (modifier != null) value += modifier.getAmount();
        attributes.applyModifier(new AttributeModifier(ATTRIBUTE_MODIFIER, "magicadaemonicosts", value, 0));
    }

}
