package net.smileycorp.magiadaemonica.common.demons.contracts;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.smileycorp.magiadaemonica.common.demons.contracts.costs.Cost;
import net.smileycorp.magiadaemonica.common.demons.contracts.offerings.Offering;

import java.util.List;
import java.util.UUID;

public class Contract {

    private UUID demon;
    private final List<Cost> costs = Lists.newArrayList();
    private final  List<Offering> offerings = Lists.newArrayList();

    public Contract(UUID demon) {
        this.demon = demon;
    }

    public Contract addCosts(Cost... costs) {
        for (Cost cost : costs) {
            if (cost == null) continue;
            this.costs.add(cost);
        }
        return this;
    }

    public Contract addOfferings(Offering... offerings) {
        for (Offering offering : offerings) {
            if (offering == null) continue;
            this.offerings.add(offering);
        }
        return this;
    }

    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setUniqueId("demon", demon);
        NBTTagList costs = new NBTTagList();
        this.costs.forEach(cost -> costs.appendTag(Cost.writeToNBT(cost)));
        nbt.setTag("costs", costs);
        NBTTagList offerings = new NBTTagList();
        this.offerings.forEach(offering -> offerings.appendTag(Offering.writeToNBT(offering)));
        nbt.setTag("offerings", offerings);
        return nbt;
    }

    public static Contract readFromNBT(NBTTagCompound nbt) {
        Contract contract = new Contract(nbt.getUniqueId("demon"));
        for (NBTBase cost : nbt.getTagList("costs", 10))
            contract.addCosts(ContractRegistry.readCost((NBTTagCompound) cost));
        for (NBTBase offering : nbt.getTagList("offerings", 10))
            contract.addOfferings(ContractRegistry.readOffering((NBTTagCompound) offering));
        return contract;
    }


}
