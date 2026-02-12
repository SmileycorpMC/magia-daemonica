package net.smileycorp.magiadaemonica.common.demons.contracts.offerings;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextComponentTranslation;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.advancements.DaemonicaAdvancements;
import net.smileycorp.magiadaemonica.common.demons.Demon;
import net.smileycorp.magiadaemonica.common.demons.contracts.ContractsUtils;
import net.smileycorp.magiadaemonica.common.items.InfernalRelic;

import java.util.Random;

public class ItemOffering implements Offering {

    public static final ResourceLocation RELIC_ID = Constants.loc("relic");
    public static ResourceLocation ID = Constants.loc("item");

    private final ItemStack stack;
    private final boolean enchanted;
    private final boolean relic;

    public ItemOffering(ItemStack stack) {
        this.stack = stack;
        this.enchanted = !stack.getEnchantmentTagList().hasNoTags();
        this.relic = stack.getItem() instanceof InfernalRelic;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return ID;
    }

    @Override
    public void grant(EntityPlayer player) {
        ItemStack stack = this.stack.copy();
        if (player.addItemStackToInventory(stack)) return;
        EntityItem item = player.dropItem(stack, false);
        if (item == null) return;
        item.setNoPickupDelay();
        item.setOwner(player.getName());
        if (relic) DaemonicaAdvancements.INFERNAL_RELIC.trigger((EntityPlayerMP) player);
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("stack", stack.writeToNBT(new NBTTagCompound()));
        return nbt;
    }

    @Override
    public Object[] getDescriptionArguments() {
        if (relic) return new Object[] {new TextComponentTranslation("contract." + Constants.MODID +
                ".offering.item.infernal_relic").getFormattedText()};
        StringBuilder builder = new StringBuilder();
        if (stack.getCount() > 1) builder.append(stack.getCount() + "x ");
        if (enchanted) builder.append(new TextComponentTranslation("contract." + Constants.MODID
                + ".offering.item.enchanted").getFormattedText() + " ");
        builder.append(stack.getDisplayName());
        return new Object[] {builder.toString()};
    }

    public static ItemOffering fromNBT(NBTTagCompound nbt) {
        return new ItemOffering(new ItemStack(nbt.getCompoundTag("stack")));
    }

    public static ItemOffering generate(Demon demon, EntityPlayer player, int tier) {
        Random rand = player.getRNG();
        //relic
        if (tier >= 3 && rand.nextInt(10) <= (tier - 3)) return new ItemOffering(ContractsUtils.getRelic(rand));
        //1 in 3 chance to offer tools/armour
        if (rand.nextInt(3) == 0) return new ItemOffering(EnchantmentHelper.addRandomEnchantment(rand,
                ContractsUtils.getEnchantableItem(rand), player.getRNG().nextInt(tier + 10) + tier * 3, true));
        EnumRarity rarity = EnumRarity.values()[MathHelper.clamp((tier + rand.nextInt((tier + 1) / 2)) / 2,  0, 3)];
        ItemStack stack = ContractsUtils.getItem(rarity, rand);
        rarity = (EnumRarity) stack.getItem().getForgeRarity(stack);
        int count = !stack.isStackable() ? 1 : (int) ((rand.nextFloat() * tier * 0.05f
                / ((float) (rarity.ordinal() * 2) + 1)) * stack.getMaxStackSize());
        stack.setCount(MathHelper.clamp(count, 1, stack.getMaxStackSize()));
        return new ItemOffering(stack);
    }

    public static ItemOffering generateRelic(Demon demon, EntityPlayer player, int tier) {
        return new ItemOffering(ContractsUtils.getRelic(player.getRNG()));
    }

}
