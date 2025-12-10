package net.smileycorp.magiadaemonica.common.demons.contracts.costs;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.smileycorp.magiadaemonica.common.demons.Demon;
import net.smileycorp.magiadaemonica.common.demons.contracts.offerings.Offering;

public interface Cost {

    ResourceLocation getRegistryName();

    void pay(EntityPlayer player);

    boolean canPay(EntityPlayer player);

    default ITextComponent getDescription() {
        ResourceLocation loc = getRegistryName();
        return new TextComponentTranslation("contract." + loc.getResourceDomain() + ".cost." + loc.getResourcePath(),
                getDescriptionArguments());
    }

    Object[] getDescriptionArguments();

    static NBTTagCompound writeToNBT(Offering offering) {
        NBTTagCompound nbt = offering.writeToNBT();
        nbt.setString("id", offering.getRegistryName().toString());
        return nbt;
    }

    NBTTagCompound writeToNBT();

    static NBTTagCompound writeToNBT(Cost cost) {
        NBTTagCompound nbt = cost.writeToNBT();
        nbt.setString("id", cost.getRegistryName().toString());
        return nbt;
    }

    interface Reader<T extends Cost> {

        T apply(NBTTagCompound nbt);

    }

    interface Generator<T extends Cost> {

        T apply(Demon demon, EntityPlayer player, int tier);

    }

    class Entry<T extends Cost> {

        private final ResourceLocation name;
        private final int tier;
        private final Reader<T> reader;
        private final Generator<T> generator;

        public Entry(ResourceLocation name, int tier, Reader<T> reader, Generator<T> generator) {
            this.name = name;
            this.tier = tier;
            this.reader = reader;
            this.generator = generator;
        }

        public ResourceLocation getName() {
            return name;
        }

        public int getTier() {
            return tier;
        }

        public Reader<T> getReader() {
            return reader;
        }

        public Generator<T> getGenerator() {
            return generator;
        }

    }

}
