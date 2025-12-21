package net.smileycorp.magiadaemonica.common.items;

import com.google.common.collect.Lists;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.common.Constants;

import javax.annotation.Nullable;
import java.util.List;

public class ItemCalixPerpetuus extends ItemRelic {

    public ItemCalixPerpetuus() {
        super("calix_perpetuus");
        addPropertyOverride(Constants.loc("filled"), (stack, entity, world) -> hasFluid(stack) ? 1 : 0);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        items.add(new ItemStack(this));
        items.add(setMilk(new ItemStack(this), true));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
        if (world.isRemote) return stack;
        if (entity instanceof EntityPlayerMP) {
            CriteriaTriggers.CONSUME_ITEM.trigger((EntityPlayerMP)entity, stack);
            ((EntityPlayerMP) entity).addStat(StatList.getObjectUseStats(this));
            ((EntityPlayerMP) entity).getCooldownTracker().setCooldown(this, 60);
        }
        if (hasMilk(stack)) {
            ItemStack milk = new ItemStack(Items.MILK_BUCKET);
            List<Potion> remove = Lists.newArrayList();
            for (PotionEffect potion : entity.getActivePotionEffects())
                if (potion.isCurativeItem(milk)) remove.add(potion.getPotion());
            for (Potion potion : remove) entity.removePotionEffect(potion);
        }
        else if (hasPotion(stack)) for (PotionEffect effect : PotionUtils.getEffectsFromStack(stack)) {
            if (!effect.getPotion().isInstant()) entity.addPotionEffect(effect);
            else effect.getPotion().affectEntity(entity, entity, entity, effect.getAmplifier(), 1);
        }
        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return hasFluid(stack) ? 32 : 0;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return hasFluid(stack) ? EnumAction.DRINK : EnumAction.NONE;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (!hasFluid(player.getHeldItem(hand))) return super.onItemRightClick(world, player, hand);
        player.setActiveHand(hand);
        return new ActionResult<>(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        if (hasMilk(stack)) tooltip.add(new TextComponentTranslation("fluid.milk")
                .setStyle(new Style().setColor(TextFormatting.WHITE)).getFormattedText());
        else if (hasPotion(stack)) PotionUtils.addPotionTooltip(stack, tooltip, 0.25F);
    }

    public static ItemStack copyEffects(ItemStack to, ItemStack from) {
        setMilk(to, false);
        PotionUtils.addPotionToItemStack(to, PotionUtils.getPotionFromItem(from));
        PotionUtils.appendEffects(to, PotionUtils.getFullEffectsFromItem(from));
        return to;
    }

    public static ItemStack setMilk(ItemStack stack, boolean milk) {
        if (milk) {
            removePotions(stack);
            if (!stack.hasTagCompound()) stack.setTagCompound(new NBTTagCompound());
            stack.getTagCompound().setBoolean("milk", true);
        } else {
            if (!stack.hasTagCompound()) return stack;
            stack.getTagCompound().removeTag("milk");
        }
        return stack;
    }

    public static void removePotions(ItemStack stack) {
        if (!stack.hasTagCompound()) return;
        NBTTagCompound nbt = stack.getTagCompound();
        nbt.removeTag("CustomPotionEffects");
        nbt.removeTag("CustomPotionColor");
        nbt.removeTag("Potion");
    }

    public static boolean hasFluid(ItemStack stack) {
        return hasMilk(stack) || hasPotion(stack);
    }

    public static boolean hasMilk(ItemStack stack) {
        if (!stack.hasTagCompound()) return false;
        NBTTagCompound nbt = stack.getTagCompound();
        if (!nbt.hasKey("milk")) return false;
        return nbt.getBoolean("milk");
    }

    public static boolean hasPotion(ItemStack stack) {
        if (hasMilk(stack)) return false;
        return !PotionUtils.getEffectsFromStack(stack).isEmpty();
    }

}
