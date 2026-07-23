package net.smileycorp.magiadaemonica.mixin;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.common.capabilities.Boons;
import net.smileycorp.magiadaemonica.common.capabilities.Curses;
import net.smileycorp.magiadaemonica.common.demons.contracts.BoonRegistry;
import net.smileycorp.magiadaemonica.common.demons.contracts.CursesRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityPlayer.class)
public abstract class MixinEntityPlayer extends EntityLivingBase{

    @Unique
    private int ticksBurning = 0;

    @Unique
    private Vec3d lastTickPos = new Vec3d(0, 0, 0);

    public MixinEntityPlayer(World worldIn) {
        super(worldIn);
    }

    @Inject(at = @At(value = "HEAD"), method = "shouldHeal", cancellable = true)
    public void magiadaemonica$shouldHeal(CallbackInfoReturnable<Boolean> callback) {
        if (Curses.has((EntityPlayer) (Object) this, CursesRegistry.TORPIDITY)) callback.setReturnValue(false);
    }

    @Inject(at = @At(value = "HEAD"), method = "onUpdate")
    public void magiadaemonica$onUpdate(CallbackInfo callback) {
        if (!world.isRemote) return;
        if (isBurning() && Boons.has((EntityPlayer) (Object) this, BoonRegistry.FLAREFOOT) &! getPositionVector().equals(lastTickPos)) ticksBurning++;
        else if (ticksBurning > 0) ticksBurning = 0;
        lastTickPos = getPositionVector();
    }

    @Inject(at = @At(value = "RETURN"), method = "getAIMoveSpeed", cancellable = true)
    public void magiadaemonica$getAIMoveSpeed(CallbackInfoReturnable<Float> callback) {
        if (!world.isRemote) return;
        if (ticksBurning < 0 |! Boons.has((EntityPlayer) (Object) this, BoonRegistry.FLAREFOOT)) return;
        callback.setReturnValue(callback.getReturnValue() * 1 + (ticksBurning * 0.0025f));
    }

}
