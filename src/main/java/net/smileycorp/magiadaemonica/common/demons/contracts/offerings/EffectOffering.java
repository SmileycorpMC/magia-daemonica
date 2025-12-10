package net.smileycorp.magiadaemonica.common.demons.contracts.offerings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.demons.Demon;

import java.util.Random;

public class EffectOffering implements Offering {

    public static ResourceLocation ID = Constants.loc("effect");

    private final Potion effect;
    private final int amplifier;

    public EffectOffering(Potion effect, int amplifier) {
        this.effect = effect;
        this.amplifier = amplifier;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return ID;
    }

    @Override
    public void grant(EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(effect, Integer.MAX_VALUE, amplifier, false, false));
    }
    @Override
    public Object[] getDescriptionArguments() {
        ITextComponent component = new TextComponentTranslation(effect.getName());
        if (amplifier > 1) component.appendSibling(new TextComponentTranslation("potion.potency." + amplifier));
        return new Object[] {component};
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("effect", effect.getRegistryName().toString());
        nbt.setDouble("amplifier", amplifier);
        return nbt;
    }

    public static EffectOffering fromNBT(NBTTagCompound nbt) {
        return new EffectOffering(ForgeRegistries.POTIONS.getValue(new ResourceLocation(nbt.getString("effect"))),
                nbt.getInteger("amplifier"));
    }

    public static EffectOffering generate(Demon demon, EntityPlayer player, int tier) {
        Random rand = player.getRNG();
        Potion[] potions = ForgeRegistries.POTIONS.getValuesCollection().toArray(new Potion[ForgeRegistries.POTIONS.getValuesCollection().size()]);
        while (true) {
            Potion potion = potions[rand.nextInt(potions.length)];
            if (tier > 3) if (potion.isBadEffect()) continue;
            if (potion.isInstant()) continue;
            return new EffectOffering(potion, (int) ((tier - rand.nextInt(tier)) * 0.25f));
        }
    }

}
