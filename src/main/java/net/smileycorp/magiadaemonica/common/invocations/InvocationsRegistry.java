package net.smileycorp.magiadaemonica.common.invocations;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.invocations.components.VocalisComponent;
import net.smileycorp.magiadaemonica.common.invocations.spell.IgniteSpell;
import net.smileycorp.magiadaemonica.common.network.InvocationMessage;
import net.smileycorp.magiadaemonica.common.rituals.summoning.SummoningCircle;

import java.util.Map;

public class InvocationsRegistry {

    private static final Map<ResourceLocation, Invocation> INVOCATIONS = Maps.newHashMap();

    public static void registerDefaults() {
        registerInvocation(Constants.loc("simple_summoning"), new RitualInvocation(SummoningCircle.ID, new VocalisComponent("te infernale( ([a-z ]*) | )invoco pacisci volo")));
        registerInvocation(Constants.loc("ignition"), new IgniteSpell());
    }

    public static void registerInvocation(ResourceLocation name, Invocation invocation) {
        invocation.setRegistryName(name);
        INVOCATIONS.put(name, invocation);
    }

    public static void processInvocation(EntityPlayerMP player, String phrase) {
        for (Invocation invocation : INVOCATIONS.values()) {
            InvocationContext ctx = new InvocationContext(phrase, player);
            if (!invocation.canApply(ctx)) continue;
            Invocation.InvocationResult result = invocation.apply(ctx);
            if (result == null) return;
            if (invocation instanceof Invocation.ClientInvocation) InvocationMessage.send(player, invocation.getRegistryName(), result.getArgs());
            invocation.consumeComponents(ctx);
        }
    }

    public static Invocation getInvocation(ResourceLocation name) {
        return INVOCATIONS.get(name);
    }

}
