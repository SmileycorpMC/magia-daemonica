package net.smileycorp.magiadaemonica.common.items;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.item.IMetaItem;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.MagiaDaemonica;

public class ItemDaemonicaEdible extends ItemFood implements IMetaItem {

    protected final String name;
    protected final float saturation;

    public ItemDaemonicaEdible(String name, int hunger, float saturation) {
        super(hunger, 0, false);
        setRegistryName(Constants.loc(name));
        setUnlocalizedName(Constants.name(name));
        setCreativeTab(MagiaDaemonica.CREATIVE_TAB);
        this.name = name;
        this.saturation = saturation;
    }

    @Override
    public float getSaturationModifier(ItemStack stack) {
        return saturation;
    }

    public boolean canEat(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World world, EntityLivingBase entity) {
        return canEat(stack) ? super.onItemUseFinish(stack, world, entity) : stack;
    }

    @Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return canEat(stack) ? super.getMaxItemUseDuration(stack) : 0;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return canEat(stack) ? super.getItemUseAction(stack) : EnumAction.NONE;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        return canEat(stack) ? super.onItemRightClick(world, player, hand) : new ActionResult<>(EnumActionResult.PASS, stack);
    }

}
