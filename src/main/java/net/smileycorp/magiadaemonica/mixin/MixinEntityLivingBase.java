package net.smileycorp.magiadaemonica.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.common.capabilities.Curses;
import net.smileycorp.magiadaemonica.common.demons.contracts.CursesRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(EntityLivingBase.class)
public abstract class MixinEntityLivingBase extends Entity {

    public MixinEntityLivingBase(World world) {
        super(world);
    }

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getSlipperiness(Lnet/minecraft/block/state/IBlockState;Lnet/minecraft/world/IBlockAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;)F"), method = "travel")
    public float magiadaemonica$travel$getSlipperiness(Block instance, IBlockState state, IBlockAccess world, BlockPos pos, Entity entity, Operation<Float> original) {
        float slipperiness = original.call(instance, state, world, pos, entity);
        if (slipperiness < 1 && ((Entity)this) instanceof EntityPlayer && Curses.has((EntityPlayer) (Entity) this, CursesRegistry.ICESOLES)) return 1;
        return slipperiness;
    }

}
