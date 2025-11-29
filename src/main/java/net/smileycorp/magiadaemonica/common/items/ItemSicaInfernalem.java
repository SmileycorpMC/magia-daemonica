package net.smileycorp.magiadaemonica.common.items;

import com.google.common.collect.Multimap;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityOwnable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.common.rituals.Ritual;
import net.smileycorp.magiadaemonica.common.rituals.Rituals;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSicaInfernalem extends ItemDaemonica {

    public ItemSicaInfernalem() {
        super("sica_infernalem");
        setMaxStackSize(1);
    }

    @Override
    public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot) {
        Multimap<String, AttributeModifier> multimap = super.getItemAttributeModifiers(equipmentSlot);
        if (equipmentSlot == EntityEquipmentSlot.MAINHAND) {
            multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", 3, 0));
            multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
        }
        return multimap;
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase entity, EntityLivingBase attacker) {
        if (entity.world.isDaytime()) return false;
        if (entity instanceof IMob || entity instanceof EntityPlayer) return false;
        if (entity instanceof IEntityOwnable) if (((IEntityOwnable) entity).getOwner() != null) return false;
        if (!(attacker instanceof EntityPlayer)) return false;
        Ritual ritual = Rituals.get(entity.world).getRitual(entity.posX, entity.posY, entity.posZ, 2);
        if (ritual == null) return false;
        entity.setDropItemsWhenDead(false);
        if (entity.world.isRemote) return false;
        entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(attacker, attacker), Integer.MAX_VALUE);
        ritual.addPower((int) (entity.getMaxHealth() * 1000));
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        tooltip.add(new TextComponentTranslation("item.magicadaemonica.sica_infernalem.tooltip").getFormattedText());
    }

}
