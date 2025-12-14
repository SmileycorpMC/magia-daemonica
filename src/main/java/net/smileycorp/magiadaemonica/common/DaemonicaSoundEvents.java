package net.smileycorp.magiadaemonica.common;

import com.google.common.collect.Sets;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Set;

@Mod.EventBusSubscriber(modid = Constants.MODID)
public class DaemonicaSoundEvents {
    
    public static final Set<SoundEvent> SOUNDS = Sets.newHashSet();

    public static final SoundEvent CONTRACT_SIGN = register("entity.contract.sign");
    public static final SoundEvent RITUAL_AMBIENCE = register("event.ritual.ambience");
    public static final SoundEvent RITUAL_CRACKLING = register("event.ritual.crackling");
    public static final SoundEvent RITUAL_CRACKLING_BASS = register("event.ritual.crackling_bass");
    public static final SoundEvent RITUAL_DEMON_SUMMON = register("event.ritual.demon_summon");
    public static final SoundEvent RITUAL_WHISPER = register("event.ritual.whisper");

    public static SoundEvent register(String name) {
        SoundEvent sound = new SoundEvent(Constants.loc(name));
        sound.setRegistryName(Constants.loc(name));
        SOUNDS.add(sound);
        return sound;
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<SoundEvent> event) {
        IForgeRegistry<SoundEvent> registry = event.getRegistry();
        SOUNDS.forEach(registry::register);
    }

}
