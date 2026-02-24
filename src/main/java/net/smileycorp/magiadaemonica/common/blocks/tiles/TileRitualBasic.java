package net.smileycorp.magiadaemonica.common.blocks.tiles;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.common.rituals.Ritual;
import net.smileycorp.magiadaemonica.common.rituals.Rituals;

public class TileRitualBasic extends TileEntity implements RitualTile {

    private BlockPos ritual;

    public TileRitualBasic(BlockPos ritual) {
        this.ritual = ritual;
    }

    @Override
    public void setRitual(BlockPos ritual) {
        this.ritual = ritual;
    }

    @Override
    public Ritual getRitual() {
        return ritual == null || world == null ? null : Rituals.get(world).getRitual(pos);
    }

    @Override
    public boolean isActive() {
        Ritual ritual = getRitual();
        return ritual != null && ritual.isActive();
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newState) {
        boolean refresh = oldState.getBlock() != newState.getBlock();
        if (refresh) Rituals.get(world).removeRitual(ritual);
        return refresh;
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        if (ritual != null) compound.setTag("ritual", NBTUtil.createPosTag(ritual));
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        if (compound.hasKey("ritual")) ritual = NBTUtil.getPosFromTag(compound.getCompoundTag("ritual"));
        return super.writeToNBT(compound);
    }

}
