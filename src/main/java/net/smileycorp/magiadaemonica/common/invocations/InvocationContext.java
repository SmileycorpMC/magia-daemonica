package net.smileycorp.magiadaemonica.common.invocations;

import com.google.common.collect.Sets;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import java.util.Set;

public class InvocationContext {

    private final Set<ResourceLocation> flags = Sets.newHashSet();
    private final String phrase;
    private final EntityPlayer player;

    public InvocationContext(String phrase, EntityPlayer player) {
        this.phrase = phrase;
        this.player = player;
    }

    public String getPhrase() {
        return phrase;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public void setFlag(ResourceLocation flag) {
        flags.add(flag);
    }

    public boolean hasFlag(ResourceLocation flag) {
        return flags.contains(flag);
    }

}
