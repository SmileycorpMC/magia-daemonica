package net.smileycorp.magiadaemonica.client.entities;

import net.minecraft.client.renderer.entity.RenderManager;
import net.smileycorp.magiadaemonica.client.entities.model.ModelDemon;
import net.smileycorp.magiadaemonica.common.entities.EntityAbstractDemon;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class RenderDemon extends GeoEntityRenderer<EntityAbstractDemon> {

    public RenderDemon(RenderManager rm) {
        super(rm, new ModelDemon());
    }

}
