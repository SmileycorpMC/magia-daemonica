package net.smileycorp.magiadaemonica.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ItemPeccatumPrimordiale extends ItemEdibleRelic{

    public ItemPeccatumPrimordiale() {
        super("peccatum_primordiale", 4, 9.6f);
        setMaxStackSize(1);
        setAlwaysEdible();
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
        for (Potion potion : ForgeRegistries.POTIONS)
            player.addPotionEffect(new PotionEffect(potion, 200));
    }

}
