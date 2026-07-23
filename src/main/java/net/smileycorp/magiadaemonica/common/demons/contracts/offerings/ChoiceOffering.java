package net.smileycorp.magiadaemonica.common.demons.contracts.offerings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.demons.Demon;
import net.smileycorp.magiadaemonica.common.demons.contracts.BoonRegistry;
import net.smileycorp.magiadaemonica.common.demons.contracts.CursesRegistry;
import net.smileycorp.magiadaemonica.common.network.ChooseCurseBoonMessage;
import net.smileycorp.magiadaemonica.common.network.ChooseRelicMessage;

import java.util.Locale;
import java.util.Random;
import java.util.function.BiConsumer;

public class ChoiceOffering implements Offering {

    public static ResourceLocation ID = Constants.loc("choice");

    private final Type type;
    private final int count;

    public ChoiceOffering(Type type, int count) {
        this.type = type;
        this.count = count;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return ID;
    }

    @Override
    public void grant(EntityPlayer player) {
        type.grant((EntityPlayerMP) player, count);
    }
    
    @Override
    public Object[] getDescriptionArguments() {
        return new Object[]{new TextComponentTranslation(type.getTranslationKey())};
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte("type", (byte) type.ordinal());
        nbt.setInteger("count", count);
        return nbt;
    }

    public static ChoiceOffering fromNBT(NBTTagCompound nbt) {
        return new ChoiceOffering(Type.get(nbt.getByte("type")), nbt.getInteger("count"));
    }

    public static ChoiceOffering generate(Demon demon, EntityPlayer player, int tier) {
        return new ChoiceOffering(Type.get(player.getRNG()), 3);
    }

    public enum Type {
        BOON((player, amount) -> ChooseCurseBoonMessage.send(player, false, BoonRegistry.getRandomBoons(player, amount))),
        CURSE((player, amount) -> ChooseCurseBoonMessage.send(player, true, CursesRegistry.getRandomCurses(player, amount))),
        RELIC(ChooseRelicMessage::send);

        private final BiConsumer<EntityPlayerMP, Integer> func;
        private final String translationKey;

        Type(BiConsumer<EntityPlayerMP, Integer> func) {
            this.func = func;
            this.translationKey = "contract.magiadaemonica.offering.choice." + name().toLowerCase(Locale.US);
        }

        public void grant(EntityPlayerMP player, int amount) {
            func.accept(player, amount);
        }

        public String getTranslationKey() {
            return translationKey;
        }

        public static Type get(byte id) {
            return values()[id % values().length];
        }

        public static Type get(Random rand) {
            return values()[rand.nextInt(values().length)];
        }

    }

}
