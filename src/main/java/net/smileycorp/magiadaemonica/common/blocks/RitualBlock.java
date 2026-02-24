package net.smileycorp.magiadaemonica.common.blocks;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.common.blocks.tiles.TileRitualBasic;
import net.smileycorp.magiadaemonica.common.demons.Domain;

import java.util.EnumMap;

public interface RitualBlock {

    default TileEntity createNewTileEntity(BlockPos ritual) {
        return new TileRitualBasic(ritual);
    }

    default int getPowerBonus(World world, BlockPos pos, EntityPlayer player) {
        return 0;
    }

    default EnumMap<Domain, Integer> getAffiliationBonus(World world, BlockPos pos, EntityPlayer player) {
        return new EnumMap<>(Domain.class);
    }

}
