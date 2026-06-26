package net.smileycorp.magiadaemonica.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.chunk.Chunk;
import net.smileycorp.magiadaemonica.common.blocks.tiles.RitualTile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Chunk.class)
public class MixinChunk {

    @WrapOperation(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;hasTileEntity(Lnet/minecraft/block/state/IBlockState;)Z"), method = "addTileEntity(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/tileentity/TileEntity;)V")
    public boolean magiadaemonica$addTileEntity$hasTileEntity(Block instance, IBlockState state, Operation<Boolean> original, @Local(argsOnly = true) TileEntity tileEntityIn) {
        return tileEntityIn instanceof RitualTile || original.call(instance, state);
    }

}
