package net.smileycorp.magiadaemonica.common.blocks;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.common.blocks.tiles.TileRitualBasic;
import net.smileycorp.magiadaemonica.common.demons.Domain;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;

public interface RitualBlock extends ITileEntityProvider {

    default TileEntity createRitualTile(World world, BlockPos ritual, IBlockState state) {
        return new TileRitualBasic(ritual);
    }

    @Nullable
    @Override
    default TileEntity createNewTileEntity(World world, int meta) {
        return null;
    }

    default int getPowerBonus(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return 0;
    }

    default EnumMap<Domain, Integer> getAffiliationBonus(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return new EnumMap<>(Domain.class);
    }

}
