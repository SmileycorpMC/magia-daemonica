package net.smileycorp.magiadaemonica.common.items.relics;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.smileycorp.magiadaemonica.config.ItemsConfig;

public class ItemPeccatumPrimordiale extends ItemEdibleRelic {

    public ItemPeccatumPrimordiale() {
        super("peccatum_primordiale", ItemsConfig.peccatumPrimordialeHunger, ItemsConfig.peccatumPrimordialeeSaturation);
        setMaxStackSize(1);
        setAlwaysEdible();
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
        for (Potion potion : ForgeRegistries.POTIONS) if (ItemsConfig.canPeccatumPrimordialeeApply(potion))
            player.addPotionEffect(new PotionEffect(potion, ItemsConfig.peccatumPrimordialeHunger));
    }

}
