package net.smileycorp.magiadaemonica.common.invocations;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayer;
import net.smileycorp.magiadaemonica.common.rituals.summoning.SummoningCircle;

import java.util.Map;

public class Invocations {

    private static final Map<String, Invocation> INVOCATIONS = Maps.newHashMap();

    static {
        registerInvocation("te infernale invoco pacisci volo", new RitualInvocation(SummoningCircle.ID));
    }

    public static void registerInvocation(String phrase, Invocation invocation) {
        INVOCATIONS.put(phrase, invocation);
    }

    public static void processInvocation(EntityPlayer player, String phrase) {
        Invocation invocation = INVOCATIONS.get(phrase);
        if (invocation != null) invocation.apply(phrase, player);
    }

}
