package net.smileycorp.magiadaemonica.common.demons;

import com.google.common.collect.Maps;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.WorldServer;
import net.smileycorp.magiadaemonica.common.WorldDataDaemonica;

import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class DemonRegistry {

    private final Map<UUID, Demon> demons = Maps.newHashMap();
    private final WorldDataDaemonica data;

    public DemonRegistry(WorldDataDaemonica data) {
        this.data = data;
    }

    public Demon get(UUID uuid) {
        return demons.get(uuid);
    }

    public void remove(Demon demon) {
        demons.remove(demon.getUUID());
        data.markDirty();
    }

    public Demon create(Random rand, int points) {
        return create(rand, points, Domain.get(rand));
    }

    public Demon create(Random rand, int points, Domain domain) {
        Rank rank = Rank.get(rand, points);
        Demon demon = new Demon(rank.getDemonName(rand, domain), domain, rank);
        Demon existing = demons.get(demon.getUUID());
        if (existing != null) return existing;
        demons.put(demon.getUUID(), demon);
        data.markDirty();
        return demon;
    }

    public void setup() {
        Random rand = new Random();
        for (Domain domain : Domain.values()) {
            Demon demon = new Demon(Rank.PRINCE.getDemonName(rand, domain), domain, Rank.PRINCE);
            demons.put(demon.getUUID(), demon);
        }
        data.markDirty();
    }

    public void readFromNBT(NBTTagCompound nbt) {
        if (!nbt.hasKey("demons")) return;
        for (NBTBase tag : nbt.getTagList("demons", 10)) {
            Demon demon = Demon.fromNBT((NBTTagCompound) tag);
            if (demon != null) demons.put(demon.getUUID(), demon);
        }
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList demons = new NBTTagList();
        for (Demon demon : this.demons.values()) demons.appendTag(demon.toNBT());
        nbt.setTag("demons", demons);
        return nbt;
    }

    public static DemonRegistry get(WorldServer world) {
        return WorldDataDaemonica.get(world).getDemonRegistry();
    }

}
