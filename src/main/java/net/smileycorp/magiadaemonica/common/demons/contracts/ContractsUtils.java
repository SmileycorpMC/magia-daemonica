package net.smileycorp.magiadaemonica.common.demons.contracts;

import net.minecraft.entity.ai.attributes.*;
import net.minecraft.entity.player.EntityPlayer;

import java.util.UUID;

public class ContractsUtils {

    private static final UUID COST = UUID.fromString("cdc852af-afe8-4eac-85bc-3f90e670a8da");
    private static final UUID BONUS = UUID.fromString("77f9ece5-8597-4b7a-8705-898598878e06");

    public static void addCostAttribute(EntityPlayer player, IAttribute attribute, double value) {
        IAttributeInstance attributes = player.getEntityAttribute(attribute);
        AttributeModifier modifier = attributes.getModifier(COST);
        if (modifier != null) value += modifier.getAmount();
        attributes.applyModifier(new AttributeModifier(COST, "magicadaemonicosts", value, 0));
    }

    public static void addBonusAttribute(EntityPlayer player, String attribute, double value) {
        AbstractAttributeMap map = player.getAttributeMap();
        IAttributeInstance attributes = map.getAttributeInstanceByName(attribute);
        if (attributes == null) attributes = map.registerAttribute(new RangedAttribute(null, attribute, 0, -Double.MAX_VALUE, Double.MAX_VALUE));
        AttributeModifier modifier = attributes.getModifier(BONUS);
        if (modifier != null) value += modifier.getAmount();
        attributes.applyModifier(new AttributeModifier(BONUS, "magicadaemonibonus", value, 0));
    }

    public static void takeExperience(EntityPlayer player, int amount) {
        while (amount > 0) {
            int cap = xpBarCap(player.experienceLevel);
            int xp = (int) (player.experience * cap);
            xp-= amount;
            if (xp > 0) {
                player.experience = (float) xp / (float) cap;
                return;
            }
            amount = -xp;
            if (amount > 0) {
                player.experienceLevel -= 1;
                cap = xpBarCap(player.experienceLevel);
                player.experience = (float) (cap - 1)/ (float) cap;
            }
        }
    }

    public static int xpBarCap(int xp) {
        return xp >= 30 ? 112 + (xp - 30) * 9 :
            xp >= 15 ? 37 + (xp - 15) * 5 : 7 + xp * 2;
    }

}
