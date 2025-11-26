package net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.offerings;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextComponentBase;
import net.smileycorp.magiadaemonica.common.rituals.summoning.bargains.BargainUtils;

public class AttributeOffering implements Offering {

    private final String attribute;
    private final double value;

    public AttributeOffering(String attribute, double value) {
        this.attribute = attribute;
        this.value = value;
    }

    @Override
    public void grant(EntityPlayer player) {
        BargainUtils.addBonusAttribute(player, attribute, value);
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
