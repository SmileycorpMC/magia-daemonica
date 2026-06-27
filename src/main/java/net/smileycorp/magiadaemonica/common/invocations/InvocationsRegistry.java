package net.smileycorp.magiadaemonica.common.invocations;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayerMP;
import net.smileycorp.magiadaemonica.common.invocations.spell.IgniteSpell;
import net.smileycorp.magiadaemonica.common.network.InvocationMessage;
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

    public static void processInvocation(EntityPlayerMP player, String phrase) {
        Invocation invocation = getInvocation(phrase);
        if (invocation == null) return;
        Invocation.InvocationResult result = invocation.apply(phrase, player);
        if (result != null && invocation instanceof Invocation.ClientInvocation) InvocationMessage.send(player, phrase, result.getArgs());
    }

    public static Invocation getInvocation(String phrase) {
        return INVOCATIONS.get(phrase);
    }

}
