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
import net.minecraftforge.common.IRarity;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.rituals.Ritual;
import net.smileycorp.magiadaemonica.common.rituals.Rituals;

import javax.annotation.Nullable;
import java.util.List;

public class ItemSicaInfernalem extends ItemKnife implements InfernalRelic {

    public ItemSicaInfernalem() {
        super("sica_infernalem", -1, 3, null);
        setMaxStackSize(1);
        addPropertyOverride(Constants.loc("glowing"), (stack, world, entity) -> {
            if (entity == null || world == null) return 0;
            if(world.getWorldTime() % 24000 < 13000) return 0;
            Ritual ritual = Rituals.get(world).getRitual(entity.posX, entity.posY, entity.posZ, 7);
            if (ritual == null) return 0;
            return ritual.isActive() ? 0 : 1;
        });
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase entity, EntityLivingBase attacker) {
        if (entity.world.isDaytime()) return false;
        if (entity instanceof IMob || entity instanceof EntityPlayer) return false;
        if (entity instanceof IEntityOwnable) if (((IEntityOwnable) entity).getOwner() != null) return false;
        if (!(attacker instanceof EntityPlayer)) return false;
        Ritual ritual = Rituals.get(entity.world).getRitual(entity.posX, entity.posY, entity.posZ, 2);
        if (ritual == null) return false;
        if (entity.world.isRemote) return false;
        entity.attackEntityFrom(DamageSource.causeIndirectMagicDamage(attacker, attacker), Integer.MAX_VALUE);
        ritual.setPower((int) (ritual.getMaxPower() * 1.1f));
        return false;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<String> tooltip, ITooltipFlag flag) {
        super.addInformation(stack, world, tooltip, flag);
        tooltip.add(new TextComponentTranslation("item.magiadaemonica.infernal_knife").getFormattedText());
        tooltip.add(new TextComponentTranslation("item.magiadaemonica." + name + ".tooltip").getFormattedText());
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        return RARITY;
    }

}
