package net.smileycorp.magiadaemonica.common.items;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.magiadaemonica.common.entities.EntityAbstractDemon;
import net.smileycorp.magiadaemonica.common.potions.DaemonicaPotions;

public class ItemFaciesExcoriata extends ItemMask {

    public ItemFaciesExcoriata() {
        super("facies_excoriata");
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        super.onArmorTick(world, player, itemStack);
        if (world.isRemote) return;
        if (player.ticksExisted % 10 != 0) return;
        RayTraceResult result = DirectionUtils.rayTrace(world, player, 32);
        if (result.typeOfHit != RayTraceResult.Type.ENTITY) return;
        if (!(result.entityHit instanceof EntityLiving)) return;
        if (result.entityHit instanceof EntityAbstractDemon) return;
        ((EntityLiving) result.entityHit).addPotionEffect(new PotionEffect(DaemonicaPotions.PETRIFIED, 40, 0));
    }

}
