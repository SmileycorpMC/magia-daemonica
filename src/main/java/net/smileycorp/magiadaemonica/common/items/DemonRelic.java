package net.smileycorp.magiadaemonica.common.items;

import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.IRarity;

public interface DemonRelic {
    IRarity RARITY = new IRarity() {
        @Override
        public TextFormatting getColor() {
            return TextFormatting.GOLD;
        }

        @Override
        public String getName() {
            return "Relic";
        }
    };
}
