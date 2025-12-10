package net.smileycorp.magiadaemonica.common.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

public class ItemPeccatumPrimordiale extends ItemDaemonicaEdible implements DemonRelic {

    public ItemPeccatumPrimordiale() {
        super("peccatum_primordiale", 4, 0.3f);
        setMaxStackSize(1);
        setAlwaysEdible();
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        tooltip.add(new TextComponentTranslation("item.magiadaemonica.peccatum_primordiale.tooltip").getFormattedText());
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        return DemonRelic.RARITY;
    }

    @Override
    protected void onFoodEaten(ItemStack stack, World world, EntityPlayer player) {
        for (Potion potion : ForgeRegistries.POTIONS)
            player.addPotionEffect(new PotionEffect(potion, 200));
    }

}
