package net.smileycorp.magiadaemonica.common.advancements;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.item.Item;

import java.lang.reflect.Field;

public class DaemonicaAdvancements {

    public static final DaemonicaCriterionTrigger SUMMON_DEMON = new DaemonicaCriterionTrigger("summon_demon");
    public static final DaemonicaCriterionTrigger DEMON_TRADE = new DaemonicaCriterionTrigger("demon_trade");
    public static final DaemonicaCriterionTrigger SUMMON_PRINCE = new DaemonicaCriterionTrigger("summon_prince");
    public static final DaemonicaCriterionTrigger INFERNAL_RELIC = new DaemonicaCriterionTrigger("infernal_relic");

    public static void register() {
        for (Field field : DaemonicaAdvancements.class.getDeclaredFields()) {
            try {
                Object object = field.get(null);
                if (!(object instanceof DaemonicaCriterionTrigger) || object == null) continue;
                CriteriaTriggers.register((DaemonicaCriterionTrigger) object);
            } catch (Exception e) {}
        }
    }

}
