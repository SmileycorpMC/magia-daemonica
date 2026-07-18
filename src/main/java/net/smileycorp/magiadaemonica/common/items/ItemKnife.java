package net.smileycorp.magiadaemonica.common.items;

import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.smileycorp.magiadaemonica.common.EnumParticle;
import net.smileycorp.magiadaemonica.common.damage.DaemonicaDamageSources;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

public class ItemKnife extends ItemDaemonica {

    private final float attackDamage;
    private final Supplier<Ingredient> reparMaterialSupplier;
    private Ingredient repairMaterial;

    public ItemKnife(String name, int durability, float attackDamage, Supplier<Ingredient> repairMaterial) {
        super(name);
        setMaxStackSize(1);
        if (durability > 0) setMaxDamage(durability);
        this.attackDamage = attackDamage;
        this.reparMaterialSupplier = repairMaterial;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        stack.damageItem(1, attacker);
        return true;
    }

    @Override
    public boolean canDestroyBlockInCreative(World world, BlockPos pos, ItemStack stack, EntityPlayer player) {
        return false;
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (reparMaterialSupplier == null) return super.getIsRepairable(toRepair, repair);
        if (repairMaterial == null) repairMaterial = reparMaterialSupplier.get();
        return repairMaterial.apply(repair);
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        if (stack.isItemStackDamageable()) stack.attemptDamageItem(1,
                FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().rand, null);
        return stack.copy();
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        tooltip.add(new TextComponentTranslation("item.magiadaemonica.knife.tooltip").getFormattedText());
        super.addInformation(stack, world, tooltip, flag);
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", attackDamage, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -1.5, 0));
        }
        return multimap;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 72000;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
        player.setActiveHand(hand);
        return new ActionResult(EnumActionResult.SUCCESS, player.getHeldItem(hand));
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityLivingBase entity, int usageTicks) {
        if (usageTicks % 10 != 0) return;
        if (stack.isItemStackDamageable()) stack.damageItem(1, entity);
        entity.attackEntityFrom(DaemonicaDamageSources.BLEED, 1);
        Random rand = entity.getRNG();
        Vec3d look = entity.getLookVec().scale(0.5f);
        Vec3d pos = new Vec3d(entity.posX + look.x, entity.posY + entity.getEyeHeight() - 0.2f + look.y, entity.posZ + look.z);
        for (int i = 0; i < 3; i++) EnumParticle.PIXEL.send(entity.dimension, pos.x + (rand.nextFloat() - 0.5) * 0.1f,
                pos.y + (rand.nextFloat() - 0.5) * 0.1f, pos.z + (rand.nextFloat() - 0.5) * 0.1f,
                (double) 0x89101C, 50d, (rand.nextFloat() - 0.5) * 0.1, -0.2, (rand.nextFloat() - 0.5) * 0.1, 0.5d);
    }

}
