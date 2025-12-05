package net.smileycorp.magiadaemonica.common.entities;

import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIDemonicTraderWatch extends EntityAIBase {

    private final EntityDemonicTrader demon;

    public EntityAIDemonicTraderWatch(EntityDemonicTrader demon) {
        this.setMutexBits(2);
        this.demon = demon;
    }

    @Override
    public boolean shouldExecute() {
        return demon.getPlayer() != null;
    }

    @Override
    public void updateTask() {
        demon.getLookHelper().setLookPositionWithEntity(demon.getPlayer(), 10, 90);
    }

}
