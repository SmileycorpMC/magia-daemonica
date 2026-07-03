package net.smileycorp.magiadaemonica.common.items;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.common.network.BlankScrollMessage;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ItemBlankScroll extends ItemDaemonica {

    public ItemBlankScroll() {
        super("blank_scroll");
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        if (world.isRemote) return super.onItemRightClick(world, player, hand);
        ItemStack stack = player.getHeldItem(hand);
        if (stack.getItem() != this) return super.onItemRightClick(world, player, hand);
        BlankScrollMessage.send(player, hand == EnumHand.MAIN_HAND);
        return new ActionResult<>(EnumActionResult.SUCCESS, stack);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(new TextComponentTranslation("item.magiadaemonica.blank_scroll.tooltip").getFormattedText());
        super.addInformation(stack, world, tooltip, flag);
    }

}
