package net.smileycorp.magiadaemonica.common.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.common.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCalixPerpetuus extends ItemRelic {

    public ItemCalixPerpetuus() {
        super("calix_perpetuus");
        addPropertyOverride(Constants.loc("filled"), (stack, entity, world) ->
                PotionUtils.getEffectsFromStack(stack).isEmpty() ? 0 : 1);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
        if (world.isRemote) return stack;
        if (entity instanceof EntityPlayerMP) {
            CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entity, stack);
            ((EntityPlayerMP) entity).addStat(StatList.getObjectUseStats(this));
            ((EntityPlayerMP) entity).getCooldownTracker().setCooldown(this, 60);
        }
        for (PotionEffect effect : PotionUtils.getEffectsFromStack(stack)) {
            if (!effect.getPotion().isInstant()) entity.addPotionEffect(effect);
            else effect.getPotion().affectEntity(entity, entity, entity, effect.getAmplifier(), 1);
        }
        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return PotionUtils.getEffectsFromStack(stack).isEmpty() ? 0 : 32;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return PotionUtils.getEffectsFromStack(stack).isEmpty() ? EnumAction.BLOCK : EnumAction.DRINK;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (PotionUtils.getEffectsFromStack(player.getHeldItem(hand)).isEmpty()) return super.onItemRightClick(world, player, hand);
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        PotionUtils.addPotionTooltip(stack, tooltip, 0.25F);
    }

    public static ItemStack copyEffects(ItemStack chalice, ItemStack potion) {
        PotionUtils.addPotionToItemStack(chalice, PotionUtils.getPotionFromItem(potion));
        PotionUtils.appendEffects(chalice, PotionUtils.getFullEffectsFromItem(potion));
        return chalice;
    }

}
