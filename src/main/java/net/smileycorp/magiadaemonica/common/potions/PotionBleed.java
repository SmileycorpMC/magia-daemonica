package net.smileycorp.magiadaemonica.common.potions;

import net.minecraft.entity.EntityLivingBase;
import net.smileycorp.magiadaemonica.common.EnumParticle;
import net.smileycorp.magiadaemonica.common.damage.DaemonicaDamageSources;

import java.util.Random;

public class PotionBleed extends DaemonicaPotion {

    protected PotionBleed() {
        super(true, 0x89101C, "bleed");
    }

    @Override
    public boolean isReady(int duration, int amplifier) {
        return duration % 10 == 0;
    }

    @Override
    public void performEffect(EntityLivingBase entity, int amplifier) {
        entity.attackEntityFrom(DaemonicaDamageSources.BLEED, amplifier + 1);
        Random rand = entity.getRNG();
        if (entity.isInvisible()) return;
        for (int i = 0; i <  3 * amplifier + rand.nextInt(4); i++) EnumParticle.PIXEL.send(entity.dimension, entity.posX + (rand.nextFloat() - 0.5) * (double)entity.width * 2,
                entity.posY + (1 + rand.nextFloat()) * 0.5 * (double)entity.height, entity.posZ + (rand.nextDouble() - 0.5) * (double)entity.width * 2,
                (double) 0x89101C, 50d, (rand.nextFloat() - 0.5) * 0.1, -0.2, (rand.nextFloat() - 0.5) * 0.1, 1d);
    }

}
