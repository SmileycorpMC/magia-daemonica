package net.smileycorp.magiadaemonica.common.demons.contracts.offerings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.smileycorp.magiadaemonica.common.demons.Demon;

public interface Offering {

    ResourceLocation getRegistryName();

    void grant(EntityPlayer player);

    NBTTagCompound writeToNBT();

    default ITextComponent getDescription() {
        ResourceLocation loc = getRegistryName();
        return new TextComponentTranslation("contract." + loc.getResourceDomain() + ".offering." + loc.getResourcePath(),
                getDescriptionArguments());
    }

    Object[] getDescriptionArguments();

    static NBTTagCompound writeToNBT(Offering offering) {
        NBTTagCompound nbt = offering.writeToNBT();
        nbt.setString("id", offering.getRegistryName().toString());
        return nbt;
    }

    interface Reader<T extends Offering> {

        T apply(NBTTagCompound nbt);

    }

    interface Generator<T extends Offering> {

        T apply(Demon demon, EntityPlayer player, int tier);

    }

    class Entry<T extends Offering> {

        private final ResourceLocation name;
        private final int tier;
        private final Offering.Reader<T> reader;
        private final Offering.Generator<T> generator;

        public Entry(ResourceLocation name, int tier, Offering.Reader<T> reader, Offering.Generator<T> generator) {
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

        public Offering.Reader<T> getReader() {
            return reader;
        }

        public Offering.Generator<T> getGenerator() {
            return generator;
        }

    }

}
