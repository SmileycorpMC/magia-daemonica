package net.smileycorp.magiadaemonica.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.common.capabilities.Boons;
import net.smileycorp.magiadaemonica.common.demons.contracts.BoonRegistry;
import net.smileycorp.magiadaemonica.common.potions.DaemonicaPotions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityPlayerMP.class)
public abstract class MixinEntityPlayerMP extends EntityPlayer {

    public MixinEntityPlayerMP(World worldIn, GameProfile gameProfileIn) {
        super(worldIn, gameProfileIn);
    }

    @Inject(at = @At(value = "HEAD"), method = "onCriticalHit")
    public void magiadaemonica$onLivingUpdate$shouldHeal(Entity entityHit, CallbackInfo callback) {
        if (!(entityHit instanceof EntityLivingBase)) return;
        int hemorrhage = Boons.getLevel(this, BoonRegistry.HEMORRHAGE);
        if (hemorrhage == 0) return;
        ((EntityLivingBase) entityHit).addPotionEffect(new PotionEffect(DaemonicaPotions.BLEED, 60, hemorrhage));
    }

}
