package net.smileycorp.magiadaemonica.client.entities.model;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.entities.EntityAbstractDemon;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import javax.annotation.Nullable;

public class ModelDemon extends AnimatedGeoModel<EntityAbstractDemon> {

    @Override
    public ResourceLocation getModelLocation(EntityAbstractDemon demon) {
        return Constants.loc("geo/lafel.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntityAbstractDemon demon) {
        return Constants.loc("textures/entities/lafel.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntityAbstractDemon demon) {
        return Constants.loc("animations/lafel.animation.json");
    }

    @Override
    public void setLivingAnimations(EntityAbstractDemon entity, Integer id, @Nullable AnimationEvent event) {
        super.setLivingAnimations(entity, id, event);
        if (event == null || Minecraft.getMinecraft().isGamePaused()) return;
        IBone head = getAnimationProcessor().getBone("head");
        EntityModelData data = (EntityModelData) event.getExtraDataOfType(EntityModelData.class).get(0);
        float yawDelta = interpolateRotation(entity.prevRenderYawOffset, entity.renderYawOffset, event.getPartialTick()) - data.netHeadYaw;
        float yaw = data.netHeadYaw + yawDelta > 0 ? 20 : -20;
        head.setRotationX(head.getRotationX() + (data.headPitch) * ((float) Math.PI / 180f));
        head.setRotationY(head.getRotationY() + yaw * ((float) Math.PI / 180f));
        head.setRotationZ(head.getRotationZ() + yawDelta > 0 ? 0.35f : -0.35f);
    }

    protected float interpolateRotation(float start, float end, float partialTicks) {
        float f;
        for (f = end - start; f < -180; f += 360) {}
        while (f >= 180f) f -= 360.0F;
        return start + partialTicks * f;
    }

}
