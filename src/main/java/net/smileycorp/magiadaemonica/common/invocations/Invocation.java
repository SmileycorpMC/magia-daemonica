package net.smileycorp.magiadaemonica.common.invocations;

import net.minecraft.entity.player.EntityPlayer;

public interface Invocation {

    void apply(String invocation, EntityPlayer player);

}
