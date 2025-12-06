package net.smileycorp.magiadaemonica.common.entities;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.common.rituals.Ritual;
import net.smileycorp.magiadaemonica.common.rituals.Rituals;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Optional;

public class EntityDemonicTrader extends EntityAbstractDemon {

    protected static final AnimationBuilder SUMMON = new AnimationBuilder().playAndHold("animation.demon.summon");
    protected static final AnimationBuilder IDLE = new AnimationBuilder().loop("animation.demon.idle");
    protected static final AnimationBuilder STATIC = new AnimationBuilder().loop("animation.demon.static");

    private final AnimationFactory factory = new AnimationFactory(this);
    public static final DataParameter<Byte> POSE = EntityDataManager.createKey(EntityDemonicTrader.class, DataSerializers.BYTE);
    private BlockPos ritual = null;
    private EntityPlayer player;

    public EntityDemonicTrader(World world) {
        super(world);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(POSE, (byte) 0);
        enablePersistence();
        setSize(1.5f, 3.2f);
    }

    @Override
    protected void applyEntityAttributes() {
        super.applyEntityAttributes();
        getAttributeMap().getAttributeInstance(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(666);
    }

    @Override
    protected void initEntityAI() {
        super.initEntityAI();
        tasks.addTask(1, new EntityAIDemonicTraderWatch(this));
        tasks.addTask(2, new EntityAIWatchClosest(this, EntityPlayer.class, 40, 1));
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (world.isRemote) return;
        if (getPose() == Pose.DESPAWNING && getAnimationTicks() > 60) setDead();
        if (getPose() == Pose.SUMMONING && getAnimationTicks() >= 100) setPose(Pose.IDLE);
    }

    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.canHarmInCreative()) super.attackEntityFrom(source, amount);
        return false;
    }

    public void setRitual(BlockPos ritual) {
        this.ritual = ritual;
    }

    public Ritual getRitual() {
        if (ritual == null) return null;
        Ritual ritual = Rituals.get(world).getRitual(this.ritual);
        if (ritual == null) this.ritual = null;
        else if (ritual.getDemon() == null) ritual.setDemon(this);
        return ritual;
    }

    public EntityPlayer getPlayer() {
        if (player != null) return player;
        Ritual ritual = getRitual();
        if (ritual == null) return null;
        Optional<EntityPlayer> optional = ritual.getPlayer();
        optional.ifPresent(entityPlayer -> player = entityPlayer);
        return player;
    }

    public void setPose(Pose pose) {
        dataManager.set(POSE, (byte)pose.ordinal());
        resetAnimation();
    }

    public Pose getPose() {
        return Pose.get(dataManager.get(POSE));
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey("pose")) dataManager.set(POSE, nbt.getByte("pose"));
        if (nbt.hasKey("ritual")) ritual = NBTUtil.getPosFromTag(nbt.getCompoundTag("ritual"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setByte("pose", dataManager.get(POSE));
        if (ritual != null) nbt.setTag("ritual", NBTUtil.createPosTag(ritual));
    }

    @Override
    protected boolean tickAnimation() {
        return getPose() != Pose.IDLE;
    }

    @Override
    public float getBrightness() {
        if (getPose() != Pose.SUMMONING) return 1;
        int ticks = getAnimationTicks();
        return ticks > 100 ? 1 : ticks > 60 ? 0.025f * (ticks - 60) : 0;
    }

    @Override
    public float getAlpha() {
        if (getPose() != Pose.DESPAWNING) return 1;
        int ticks = getAnimationTicks();
        return ticks > 60 ? 0 : 0.01667f * (60 - ticks);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "main", 0, this::getAnimation));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    private PlayState getAnimation(AnimationEvent<EntityDemonicTrader> event) {
        AnimationController controller = event.getController();
        switch (getPose()) {
            case SUMMONING:
                if (getAnimationTicks() >= 100) return PlayState.STOP;
                controller.setAnimation(SUMMON);
                break;
            case DESPAWNING:
                controller.setAnimation(STATIC);
                break;
            default:
                controller.setAnimation(IDLE);
                break;
        }
        return PlayState.CONTINUE;
    }

    public enum Pose {
        SUMMONING,
        IDLE,
        DESPAWNING;

        public static Pose get(byte b) {
            return values()[b % values().length];
        }

    }

}
