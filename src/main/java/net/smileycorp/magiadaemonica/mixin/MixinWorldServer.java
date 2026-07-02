package net.smileycorp.magiadaemonica.mixin;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.profiler.Profiler;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import net.smileycorp.magiadaemonica.common.capabilities.Curses;
import net.smileycorp.magiadaemonica.common.demons.contracts.CursesRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WorldServer.class)
public abstract class MixinWorldServer extends World {

    protected MixinWorldServer(ISaveHandler saveHandlerIn, WorldInfo info, WorldProvider providerIn, Profiler profilerIn, boolean client) {
        super(saveHandlerIn, info, providerIn, profilerIn, client);
    }

    @Inject(at = @At("HEAD"), method = "adjustPosToNearbyEntity", cancellable = true)
    public void magiadaemonica$adjustPosToNearbyEntity(BlockPos pos, CallbackInfoReturnable<BlockPos> callback) {
        double closestDistance = 0;
        EntityPlayer closest = null;
        for (EntityPlayer player : playerEntities) {
            double distance = player.getDistanceSqToCenter(pos);
            int lightningRod = Curses.getLevel(player, CursesRegistry.LIGHTNINGROD);
            if (lightningRod == 0) return;
            if (distance > lightningRod * lightningRod * 1024) continue;
            if (closestDistance != 0 && closestDistance < distance) continue;
            closestDistance = distance;
            closest = player;
        }
        if (closest != null) callback.setReturnValue(closest.getPosition());
    }

}
