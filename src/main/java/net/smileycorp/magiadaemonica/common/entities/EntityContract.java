package net.smileycorp.magiadaemonica.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.smileycorp.magiadaemonica.common.demons.Demon;
import net.smileycorp.magiadaemonica.common.demons.contracts.Contract;
import net.smileycorp.magiadaemonica.common.demons.contracts.ContractsUtils;
import net.smileycorp.magiadaemonica.common.network.OpenContractMessage;
import net.smileycorp.magiadaemonica.common.network.ValidateContractMessage;
import net.smileycorp.magiadaemonica.common.rituals.Ritual;
import net.smileycorp.magiadaemonica.common.rituals.Rituals;
import net.smileycorp.magiadaemonica.common.rituals.summoning.SummoningCircle;

public class EntityContract extends Entity {

    public static final DataParameter<Demon> DEMON = EntityDataManager.createKey(EntityAbstractDemon.class, Demon.SERIALIZER);

    private Contract contract;
    private BlockPos ritual = null;
    private boolean accepted;

    public EntityContract(World worldIn) {
        super(worldIn);
        setSize(0.5f, 0.5f);
    }

    @Override
    protected void entityInit() {
        dataManager.register(DEMON, Demon.DEFAULT);
    }

    @Override
    public boolean processInitialInteract(EntityPlayer player, EnumHand hand) {
        if (world.isRemote || contract == null ||  accepted) return true;
        OpenContractMessage.send(player, this);
        return super.processInitialInteract(player, hand);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.canHarmInCreative()) super.attackEntityFrom(source, amount);
        return false;
    }

    @Override
    public boolean hasNoGravity() {
        return true;
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
    }

    public void accept(EntityPlayerMP player) {
        if (!contract.canPay(player)) return;
        if (player.getHealth() <= 0.5) return;
        player.attackEntityFrom(DamageSource.causeIndirectMagicDamage(player, null), 0.5f);
        accepted = true;
        ValidateContractMessage.send(player, this);
        ContractsUtils.addAffinity(player, getDemon());
        contract.accept(player);
        getRitual().dispel(world);
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

    public void setContract(Contract contract) {
        this.contract = contract;
        if (world instanceof WorldServer) dataManager.set(DEMON, contract.getDemon((WorldServer) world));
    }

    public Contract getContract() {
        return contract;
    }

    public Demon getDemon() {
        return dataManager.get(DEMON);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("contract")) setContract(Contract.readFromNBT(nbt.getCompoundTag("contract")));
        if (nbt.hasKey("ritual")) ritual = NBTUtil.getPosFromTag(nbt.getCompoundTag("ritual"));
        if (nbt.hasKey("accepted")) accepted = nbt.getBoolean("accepted");
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        if (contract != null) nbt.setTag("contract", contract.writeToNBT());
        if (ritual != null) nbt.setTag("ritual", NBTUtil.createPosTag(ritual));
        nbt.setBoolean("accepted", accepted);
    }

}
