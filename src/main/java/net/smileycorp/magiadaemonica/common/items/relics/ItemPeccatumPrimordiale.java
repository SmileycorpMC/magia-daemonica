package net.smileycorp.magiadaemonica.common.items.relics;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.smileycorp.magiadaemonica.common.DaemonicaAttributes;
import net.smileycorp.magiadaemonica.common.demons.Rank;
import net.smileycorp.magiadaemonica.config.ItemsConfig;

import java.util.UUID;

public class ItemPeccatumPrimordiale extends ItemEdibleRelic {

    private static final UUID AFFINITY = UUID.fromString("fba22b4a-ad87-4af9-aa4e-272e2cef259f");

    public ItemPeccatumPrimordiale() {
        super("peccatum_primordiale", ItemsConfig.peccatumPrimordialeHunger, ItemsConfig.peccatumPrimordialeSaturation);
        setMaxStackSize(1);
        setAlwaysEdible();
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
        for (Potion potion : ForgeRegistries.POTIONS) if (ItemsConfig.canPeccatumPrimordialeApply(potion))
            player.addPotionEffect(new PotionEffect(potion, ItemsConfig.peccatumPrimordialeDuration));
        IAttributeInstance attributes = player.getEntityAttribute(DaemonicaAttributes.INFERNAL_AFFINITY);
        if (attributes.getModifier(AFFINITY) != null) return;
        attributes.applyModifier(new AttributeModifier(AFFINITY, "peccatum_primordiale", ItemsConfig.peccatumPrimordialeAffinityBoost, 0));
    }

}
