package net.smileycorp.magiadaemonica.common.items.relics;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.magiadaemonica.common.entities.EntityAbstractDemon;
import net.smileycorp.magiadaemonica.common.potions.DaemonicaPotions;
import net.smileycorp.magiadaemonica.config.ItemsConfig;

public class ItemFaciesExcoriata extends ItemInfernalMask {

    public ItemFaciesExcoriata() {
        super("facies_excoriata");
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        super.onArmorTick(world, player, itemStack);
        if (world.isRemote) return;
        if (player.ticksExisted % ItemsConfig.faciesExcoriataTickRate != 0) return;
        RayTraceResult result = DirectionUtils.rayTrace(world, player, ItemsConfig.faciesExcoriataRange);
        if (result.typeOfHit != RayTraceResult.Type.ENTITY) return;
        if (!(result.entityHit instanceof EntityLiving)) return;
        EntityLiving entity = (EntityLiving) result.entityHit;
        if (entity instanceof EntityAbstractDemon) return;
        if (ItemsConfig.isImmuneToFaciesExcoriata(entity)) return;
        entity.addPotionEffect(new PotionEffect(DaemonicaPotions.PETRIFIED, ItemsConfig.faciesExcoriataDuration));
    }

}
