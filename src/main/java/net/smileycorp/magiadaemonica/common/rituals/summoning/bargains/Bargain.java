package net.smileycorp.magiadaemonica.common.rituals.summoning.bargains;

import net.minecraft.nbt.NBTTagCompound;
import net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.costs.Cost;
import net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.offerings.Offering;

import java.util.UUID;

public class Bargain {

    private UUID demon;
    private Cost[] costs = {};
    private Offering[] offerings = {};

    public Bargain(UUID demon) {
        this.demon = demon;
    }

    public Bargain addCosts(Cost... costs) {
        Cost[] old = this.costs;
        this.costs = new Cost[costs.length + old.length];
        for (int i = 0; i < old.length; i++) this.costs[i] = old[i];
        for (int i = 0; i < costs.length; i++) this.costs[i + old.length] = costs[i];
        return this;
    }

    public Bargain addOfferings(Offering... offerings) {
        Offering[] old = this.offerings;
        this.offerings = new Offering[offerings.length + old.length];
        for (int i = 0; i < old.length; i++) this.offerings[i] = old[i];
        for (int i = 0; i < offerings.length; i++) this.offerings[i + old.length] = offerings[i];
        return this;
    }

    public void readFromNBT(NBTTagCompound nbt) {

    }


}
