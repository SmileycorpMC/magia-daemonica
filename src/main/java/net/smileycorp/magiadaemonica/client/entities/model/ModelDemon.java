package net.smileycorp.magiadaemonica.client.entities.model;

import net.minecraft.util.ResourceLocation;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.entities.EntityAbstractDemon;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ModelDemon extends AnimatedGeoModel<EntityAbstractDemon> {

    @Override
    public ResourceLocation getModelLocation(EntityAbstractDemon demon) {
        return Constants.loc("geo/bahamut.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAbstractDemon demon) {
        return Constants.loc("textures/entities/bahamut.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityAbstractDemon demon) {
        return Constants.loc("animations/bahamut.animation.json");
    }

}
