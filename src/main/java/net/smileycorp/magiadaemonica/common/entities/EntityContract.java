package net.smileycorp.magiadaemonica.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.common.demons.contracts.Contract;
import net.smileycorp.magiadaemonica.common.demons.contracts.ContractRegistry;
import net.smileycorp.magiadaemonica.common.rituals.Ritual;
import net.smileycorp.magiadaemonica.common.rituals.Rituals;
import net.smileycorp.magiadaemonica.common.rituals.summoning.SummoningCircle;

public class EntityContract extends Entity {

    private Contract contract;
    private BlockPos ritual = null;

    public EntityContract(World worldIn) {
        super(worldIn);
    }

    @Override
    protected void entityInit() {

    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public boolean canBePushed() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("contract")) contract = Contract.readFromNBT(nbt.getCompoundTag("contract"));
        if (nbt.hasKey("ritual")) ritual = NBTUtil.getPosFromTag(nbt.getCompoundTag("ritual"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        if (contract != null) nbt.setTag("contract", contract.writeToNBT());
        if (ritual != null) nbt.setTag("ritual", NBTUtil.createPosTag(ritual));
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.canHarmInCreative()) super.attackEntityFrom(source, amount);
        return false;
    }

    public void setRitual(BlockPos ritual) {
        this.ritual = ritual;
    }

    public SummoningCircle getRitual() {
        if (ritual == null) return null;
        Ritual ritual = Rituals.get(world).getRitual(this.ritual);
        if (!(ritual instanceof SummoningCircle)) this.ritual = null;
        return (SummoningCircle) ritual;
    }

}
