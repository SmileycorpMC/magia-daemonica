package net.smileycorp.magiadaemonica.common.demons.contracts.offerings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.capabilities.Boons;
import net.smileycorp.magiadaemonica.common.demons.Demon;
import net.smileycorp.magiadaemonica.common.demons.contracts.BoonRegistry;

import java.util.List;
import java.util.Random;

public class BoonOffering implements Offering {

    public static ResourceLocation ID = Constants.loc("boon");

    private final ResourceLocation boon;

    public BoonOffering(ResourceLocation boon) {
        this.boon = boon;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return ID;
    }

    @Override
    public void grant(EntityPlayer player) {
        Boons.add(player, boon);
    }
    
    @Override
    public Object[] getDescriptionArguments() {
        String key = "boon." + boon.getResourceDomain() + "." + boon.getResourcePath();
        return new Object[]{new TextComponentTranslation(key),
                new TextComponentTranslation(key + ".description")};
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("boon", boon.toString());
        return nbt;
    }

    public static BoonOffering fromNBT(NBTTagCompound nbt) {
        return new BoonOffering(new ResourceLocation(nbt.getString("boon")));
    }

    public static BoonOffering generate(Demon demon, EntityPlayer player, int tier) {
        List<ResourceLocation> boons = BoonRegistry.getApplicableBoons(player);
        if (boons.isEmpty()) boons.addAll(BoonRegistry.getBoonNames());
        return new BoonOffering(boons.get(player.getRNG().nextInt(boons.size())));
    }

}
