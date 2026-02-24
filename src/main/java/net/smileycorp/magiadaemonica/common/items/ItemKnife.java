package net.smileycorp.magiadaemonica.common.items;

import com.google.common.collect.Multimap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.function.Supplier;

public class ItemKnife extends ItemDaemonica {

    private final float attackDamage;
    private final Supplier<Ingredient> reparMaterialSupplier;
    private Ingredient reparMaterial;

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
        if (reparMaterial == null) reparMaterial = reparMaterialSupplier.get();
        return reparMaterial.apply(repair);
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        if (stack.isItemStackDamageable()) stack.attemptDamageItem(1,
                FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld().rand, null);
        System.out.println(stack);
        return stack.copy();
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
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

}
