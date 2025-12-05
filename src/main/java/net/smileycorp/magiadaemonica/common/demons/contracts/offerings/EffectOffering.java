package net.smileycorp.magiadaemonica.common.demons.contracts.offerings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentBase;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.smileycorp.magiadaemonica.common.Constants;

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
    public TextComponentBase getDescription(int tier) {
        return null;
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

}
