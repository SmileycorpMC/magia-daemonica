package net.smileycorp.magiadaemonica.common.demons;

import com.google.common.collect.Maps;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.smileycorp.magiadaemonica.common.WorldDataDaemonica;
import net.smileycorp.magiadaemonica.common.demons.contracts.ContractsUtils;
import net.smileycorp.magiadaemonica.common.demons.contracts.costs.Cost;
import net.smileycorp.magiadaemonica.common.demons.contracts.costs.ExperienceLevelCost;
import net.smileycorp.magiadaemonica.common.demons.contracts.costs.ItemCost;
import net.smileycorp.magiadaemonica.common.demons.contracts.offerings.ChoiceOffering;
import net.smileycorp.magiadaemonica.common.demons.contracts.offerings.Offering;
import net.smileycorp.magiadaemonica.common.items.DaemonicaItems;

import java.util.EnumMap;
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
        Demon demon = demons.get(uuid);
        return demon == null ? Demon.DEFAULT : demon;
    }

    public void remove(Demon demon) {
        demons.remove(demon.getUUID());
        data.markDirty();
    }

    public Demon create(Random rand, int points) {
        return create(rand, points, Domain.get(rand));
    }

    public Demon create(Random rand, int points, EnumMap<Domain, Integer> affiliation) {
        return create(rand, points, Domain.get(rand, affiliation));
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
            if (demons.containsKey(demon.getUUID())) {
                demon = demons.get(demon);
                demon.getCustomContracts().clear();
            }
            else demons.put(demon.getUUID(), demon);
        }
        Demon azazel = new Demon("azazel", null, null);
        if (demons.containsKey(azazel.getUUID())) {
            azazel = demons.get(azazel.getUUID());
            azazel.getCustomContracts().clear();
            azazel.addCustomContract(new Cost[]{new ItemCost(new ItemStack(DaemonicaItems.MATERIAL, 5, 666)), new ExperienceLevelCost(10)},
                    new Offering[]{new ChoiceOffering(ChoiceOffering.Type.RELIC, ContractsUtils.getRelics().size())});
        }
        data.markDirty();
    }

    public void readFromNBT(NBTTagCompound nbt) {
        if (!nbt.hasKey("demons")) return;
        for (NBTBase tag : nbt.getTagList("demons", 10)) {
            Demon demon = Demon.fromNBT((NBTTagCompound) tag);
            if (demon != null) demons.put(demon.getUUID(), demon);
        }
        if (!nbt.hasKey("version") || nbt.getInteger("version") == 1) setup();
    }

    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagList demons = new NBTTagList();
        for (Demon demon : this.demons.values()) demons.appendTag(demon.toNBT());
        nbt.setTag("demons", demons);
        nbt.setInteger("version", 1);
        return nbt;
    }

    public static DemonRegistry get() {
        return WorldDataDaemonica.get().getDemonRegistry();
    }

}
