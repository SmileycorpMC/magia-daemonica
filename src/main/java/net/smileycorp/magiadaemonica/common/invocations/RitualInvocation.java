package net.smileycorp.magiadaemonica.common.invocations;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.magiadaemonica.common.invocations.components.MagiaComponent;
import net.smileycorp.magiadaemonica.common.rituals.Ritual;
import net.smileycorp.magiadaemonica.common.rituals.Rituals;

public class RitualInvocation extends Invocation {

    private final ResourceLocation ritualType;

    public RitualInvocation(ResourceLocation ritualType, MagiaComponent... components) {
        this.ritualType = ritualType;
        for (MagiaComponent component : components) addComponent(component);
    }

    @Override
    public InvocationResult apply(InvocationContext ctx) {
        EntityPlayer player = ctx.getPlayer();
        for (Ritual ritual : Rituals.get(player.world).getRituals()) {
            if (!ritual.getID().equals(ritualType)) continue;
            int disSqr = ((ritual.getWidth() / 2) + 2) * ((ritual.getHeight() / 2) + 2);
            if (ritual.getCenterPos().distanceSqToCenter(player.posX, player.posY, player.posZ) > disSqr) continue;
            return InvocationResult.optionalSuccess(ritual.processInvocation(player, ctx.getPhrase()));
        }
        return null;
    }

}
