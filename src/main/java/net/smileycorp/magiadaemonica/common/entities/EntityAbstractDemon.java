package net.smileycorp.magiadaemonica.common.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.smileycorp.magiadaemonica.common.demons.Demon;
import net.smileycorp.magiadaemonica.common.demons.DemonRegistry;
import software.bernie.geckolib3.core.IAnimatable;

import java.util.UUID;

public abstract class EntityAbstractDemon extends EntityLiving implements IAnimatable {

    public static final DataParameter<Demon> DEMON = EntityDataManager.createKey(EntityAbstractDemon.class, Demon.SERIALIZER);
    public static final DataParameter<Integer> ANIMATION_TICKS = EntityDataManager.createKey(EntityAbstractDemon.class, DataSerializers.VARINT);

    public EntityAbstractDemon(World world) {
        super(world);
    }

    @Override
    protected void updateAITasks() {
        super.updateAITasks();
        if (world.isRemote) return;
        if (tickAnimation()) dataManager.set(ANIMATION_TICKS, getAnimationTicks() + 1);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(DEMON, Demon.DEFAULT);
        dataManager.register(ANIMATION_TICKS, 0);
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
    public void collideWithEntity(Entity entity) {}

    public Demon getDemon() {
        return dataManager.get(DEMON);
    }

    public void setDemon(Demon demon) {
        dataManager.set(DEMON, demon);
    }

    @Override
    public void readEntityFromNBT(NBTTagCompound nbt) {
        super.readEntityFromNBT(nbt);
        if (nbt.hasKey("demonID") && world instanceof WorldServer)
            setDemon(DemonRegistry.get().get(UUID.fromString(nbt.getString("demonID"))));
        if (nbt.hasKey("animationTicks")) dataManager.set(ANIMATION_TICKS, nbt.getInteger("animationTicks"));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setString("demonID", getDemon().getUUID().toString());
        nbt.setInteger("animationTicks", getAnimationTicks());
    }

    public void resetAnimation() {
        dataManager.set(ANIMATION_TICKS, 0);
    }

    public int getAnimationTicks() {
        return dataManager.get(ANIMATION_TICKS);
    }

    @Override
    public String getName() {
        String name = getDemon().getName();
        return name.isEmpty() ? super.getName() : name;
    }

    protected abstract boolean tickAnimation();

    public float getBrightness() {
        return 1;
    }

    public float getAlpha() {
        return 1;
    }

}
