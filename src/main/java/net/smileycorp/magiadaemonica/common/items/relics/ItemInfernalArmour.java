package net.smileycorp.magiadaemonica.common.items.relics;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.common.util.EnumHelper;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.MagiaDaemonica;

import javax.annotation.Nullable;
import java.util.List;

public class ItemInfernalArmour extends ItemArmor implements InfernalRelic {

    private final String name;
    private final String texture;

    public ItemInfernalArmour(String name, EntityEquipmentSlot slot, int armor, int toughness, SoundEvent equipSound) {
        super(EnumHelper.addArmorMaterial(name, Constants.locStr(name), -1, new int[] {armor, armor, armor, armor}, 0, equipSound, toughness), slot == EntityEquipmentSlot.LEGS ? 1 : 0, slot);
        setRegistryName(Constants.loc(name));
        setUnlocalizedName(Constants.name(name));
        setCreativeTab(MagiaDaemonica.CREATIVE_TAB);
        this.name = name;
        this.texture = Constants.locStr("textures/models/armor/" + name + ".png");
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        tooltip.add(new TextComponentTranslation("item.magiadaemonica.infernal_armour_" + getEquipmentSlot().getName()).getFormattedText());
        tooltip.add(new TextComponentTranslation("item.magiadaemonica." + name + ".tooltip").getFormattedText());
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        return RARITY;
    }

    @Nullable
    public String getArmorTexture(ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type) {
        return texture;
    }

}
