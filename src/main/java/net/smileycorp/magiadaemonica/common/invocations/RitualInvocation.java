package net.smileycorp.magiadaemonica.common.invocations;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.magiadaemonica.common.rituals.Ritual;
import net.smileycorp.magiadaemonica.common.rituals.Rituals;

public class RitualInvocation implements Invocation {

    private final ResourceLocation ritualType;

    public RitualInvocation(ResourceLocation ritualType) {
        this.ritualType = ritualType;
    }

    @Override
    public void apply(String invocation, EntityPlayer player) {
        for (Ritual ritual : Rituals.get(player.world).getRituals()) {
            if (!ritual.getID().equals(ritualType)) continue;
            int disSqr = ((ritual.getWidth() / 2) + 2) * ((ritual.getHeight() / 2) + 2);
            if (ritual.getCenterPos().distanceSqToCenter(player.posX, player.posY, player.posZ) > disSqr) continue;
            ritual.processInvocation(player, invocation);
            return;
        }
    }

}
