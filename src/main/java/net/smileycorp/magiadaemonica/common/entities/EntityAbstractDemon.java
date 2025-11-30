package net.smileycorp.magiadaemonica.common.entities;

import net.minecraft.entity.EntityLiving;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.smileycorp.magiadaemonica.common.demons.Demon;
import net.smileycorp.magiadaemonica.common.demons.DemonRegistry;
import software.bernie.geckolib3.core.IAnimatable;

import java.util.UUID;

public abstract class EntityAbstractDemon extends EntityLiving implements IAnimatable {

    public static final DataParameter<Demon> DEMON = EntityDataManager.createKey(EntityAbstractDemon.class, Demon.SERIALIZER);

    public EntityAbstractDemon(World world) {
        super(world);
    }

    @Override
    protected void entityInit() {
        super.entityInit();
        dataManager.register(DEMON, Demon.DEFAULT);
    }

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
            setDemon(DemonRegistry.get((WorldServer) world).get(UUID.fromString(nbt.getString("demonID"))));
    }

    @Override
    public void writeEntityToNBT(NBTTagCompound nbt) {
        super.writeEntityToNBT(nbt);
        nbt.setString("demonID", getDemon().getUUID().toString());
    }

    @Override
    public String getName() {
        String name = getDemon().getName();
        return name.isEmpty() ? super.getName() : name;
    }

}
