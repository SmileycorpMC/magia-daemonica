package net.smileycorp.magiadaemonica.mixin;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.common.capabilities.Curses;
import net.smileycorp.magiadaemonica.common.capabilities.Effects;
import net.smileycorp.magiadaemonica.common.demons.contracts.BoonRegistry;
import net.smileycorp.magiadaemonica.common.demons.contracts.CursesRegistry;
import net.smileycorp.magiadaemonica.common.util.Effect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase{

    public MixinEntityPlayer(World worldIn) {
        super(worldIn);
    }

    @Inject(at = @At(value = "HEAD"), method = "shouldHeal", cancellable = true)
    public void magiadaemonica$shouldHeal(CallbackInfoReturnable<Boolean> callback) {
        if (Curses.has((EntityPlayer) (Object) this, CursesRegistry.TORPIDITY)) callback.setReturnValue(false);
    }

    @Inject(at = @At(value = "RETURN"), method = "getAIMoveSpeed", cancellable = true)
    public void magiadaemonica$getAIMoveSpeed(CallbackInfoReturnable<Float> callback) {
        if (!world.isRemote) return;
        Effect effect = Effects.get((EntityPlayer) (Object) this, BoonRegistry.FLAREFOOT);
        if (effect == null) return;
        callback.setReturnValue(callback.getReturnValue() * 1 + (effect.getLevel() * 0.05f));
    }

}
