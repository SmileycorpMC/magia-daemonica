package net.smileycorp.magiadaemonica.common.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.common.network.FillChatMessage;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemInscribedScroll extends ItemDaemonica {

    public ItemInscribedScroll() {
        super("inscribed_scroll");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (world.isRemote) return super.onItemRightClick(world, player, hand);
        ItemStack stack = player.getHeldItem(hand);
        String inscription = getInscription(stack);
        if (inscription != null) {
            if (player instanceof EntityPlayerMP) {
                FillChatMessage.send(player, inscription);
                player.addStat(StatList.getObjectUseStats(this));
                player.getCooldownTracker().setCooldown(this, 60);
            }
            return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
        }
        return super.onItemRightClick(world, player, hand);
    }

    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
        if (!isInCreativeTab(tab)) return;
        items.add(inscribeScroll( new ItemStack(this), "te infernale invoco pacisci volo"));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        String inscription = getInscription(stack);
        if (inscription != null) {
            TextComponentString component = new TextComponentString(inscription);
            component.setStyle(new Style().setItalic(true).setColor(TextFormatting.WHITE));
            tooltip.add(new TextComponentTranslation("item.magiadaemonica.inscribed_scroll.tooltip", component).getFormattedText());
        }
        tooltip.add(new TextComponentTranslation("item.magiadaemonica.inscribed_scroll.tooltip1").getFormattedText());
        super.addInformation(stack, world, tooltip, flag);
    }

    public static String getInscription(ItemStack stack) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) return null;
        return nbt.getString("inscription");
    }

    public static ItemStack inscribeScroll(ItemStack stack, String inscription) {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) nbt = new NBTTagCompound();
        nbt.setString("inscription", inscription);
        stack.setTagCompound(nbt);
        return stack;
    }

}
