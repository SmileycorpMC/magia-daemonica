package net.smileycorp.magiadaemonica.common.demons.contracts.offerings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentBase;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.demons.contracts.ContractsUtils;

public class AttributeOffering implements Offering {

    public static ResourceLocation ID = Constants.loc("attribute");

    private final String attribute;
    private final double value;

    public AttributeOffering(String attribute, double value) {
        this.attribute = attribute;
        this.value = value;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return ID;
    }

    @Override
    public void grant(EntityPlayer player) {
        ContractsUtils.addBonusAttribute(player, attribute, value);
    }

    @Override
    public TextComponentBase getDescription(int tier) {
        return null;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("attribute", attribute);
        nbt.setDouble("value", value);
        return nbt;
    }

    public static AttributeOffering fromNBT(NBTTagCompound nbt) {
        return new AttributeOffering(nbt.getString("attribute"), nbt.getDouble("value"));
    }

}
