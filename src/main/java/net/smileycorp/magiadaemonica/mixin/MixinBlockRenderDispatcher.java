package net.smileycorp.magiadaemonica.mixin;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.smileycorp.magiadaemonica.common.blocks.RitualBlock;
import net.smileycorp.magiadaemonica.common.blocks.tiles.RitualTile;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockRendererDispatcher.class)
public class MixinBlockRenderDispatcher {

    @Inject(at = @At(value = "HEAD"), method = "renderBlock", cancellable = true)
    private void magiadaemonica$renderBlock(IBlockState state, BlockPos pos, IBlockAccess world, BufferBuilder buffer, CallbackInfoReturnable<Boolean> callback) {
        if (!(state.getBlock() instanceof RitualBlock)) return;
        TileEntity tile = world.getTileEntity(pos);
        if (!(tile instanceof RitualTile)) return;
        if (!((RitualTile) tile).renderBlock())callback.setReturnValue(false);
    }

}
