package net.smileycorp.magiadaemonica.common.invocations;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.smileycorp.magiadaemonica.common.invocations.spell.IgniteSpell;
import net.smileycorp.magiadaemonica.common.rituals.summoning.SummoningCircle;

import java.util.Map;

public class InvocationsRegistry {

    private static final Map<String, Invocation> INVOCATIONS = Maps.newHashMap();

    public static void registerDefaults() {
        registerInvocation("te infernale invoco pacisci volo", new RitualInvocation(SummoningCircle.ID));
        registerInvocation("scintilla in ignem", new IgniteSpell());
    }

    public static void registerInvocation(String phrase, Invocation invocation) {
        INVOCATIONS.put(phrase, invocation);
    }

    public static void processInvocation(EntityPlayer player, String phrase) {
        Invocation invocation = INVOCATIONS.get(phrase);
        if (invocation != null) invocation.apply(phrase, player);
    }

}
