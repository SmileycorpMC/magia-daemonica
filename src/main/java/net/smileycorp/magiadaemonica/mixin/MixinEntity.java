package net.smileycorp.magiadaemonica.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.smileycorp.magiadaemonica.common.capabilities.Boons;
import net.smileycorp.magiadaemonica.common.capabilities.Curses;
import net.smileycorp.magiadaemonica.common.damage.DaemonicaDamageSources;
import net.smileycorp.magiadaemonica.common.demons.contracts.BoonRegistry;
import net.smileycorp.magiadaemonica.common.demons.contracts.CursesRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class MixinEntity {

    @Shadow public boolean onGround;

    @Shadow public int ticksExisted;

    @Shadow public abstract boolean isRiding();

    @Shadow public abstract boolean attackEntityFrom(DamageSource source, float amount);

    @Shadow public double motionX;

    @Shadow public double motionZ;

    @Unique
    private double xMotion;

    @Unique
    private double zMotion;

    @Inject(at = @At(value = "HEAD"), method = "move")
    public void magiadaemonica$move(MoverType type, double x, double y, double z, CallbackInfo callback) {
        if (type != MoverType.PLAYER || (x == 0 && z == 0) || ticksExisted % 10 != 0) return;
        if (isRiding() |! onGround || y > 0) return;
        int knifestep = Curses.getLevel((EntityPlayer) (Object)this, CursesRegistry.KNIFESTEP);
        if (knifestep == 0) return;
        attackEntityFrom(DaemonicaDamageSources.KNIFESTEP, (float) Math.ceil(knifestep * Math.sqrt(x * x + z * z) * 4));
    }

    @Inject(at = @At(value = "HEAD"), method = "doBlockCollisions")
    public void magiadaemonica$doBlockCollisions$HEAD(CallbackInfo callback) {
        if (!(((Object)this) instanceof EntityPlayer)) return;
        if (!Boons.has((EntityPlayer)(Object)this, BoonRegistry.JUGGERNAUT)) return;
        xMotion = motionX;
        zMotion = motionZ;
    }

    @Inject(at = @At(value = "RETURN"), method = "doBlockCollisions")
    public void magiadaemonica$doBlockCollisions$RETURN(CallbackInfo callback) {
        if (Math.abs(motionX) < Math.abs(xMotion)) {
            motionX = xMotion;
            xMotion = 0;
        }
        if (Math.abs(motionZ) < Math.abs(zMotion)) {
            motionZ = zMotion;
            zMotion = 0;
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "setInWeb", cancellable = true)
    public void magiadaemonica$setInWeb(CallbackInfo callback) {
        if (!(((Object)this) instanceof EntityPlayer)) return;
        if (Boons.has((EntityPlayer)(Object)this, BoonRegistry.JUGGERNAUT)) callback.cancel();
    }

}
