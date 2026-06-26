package net.smileycorp.magiadaemonica.common.rituals.summoning;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.smileycorp.magiadaemonica.common.rituals.PatternMatcher;

public class SummoningCirclePattern extends PatternMatcher<SummoningCircle> {

    private final ResourceLocation name;

    public SummoningCirclePattern(ResourceLocation name, boolean rotate, boolean mirror, int[][] pattern, PatternMatcher.Key[] keys) {
        super(rotate, mirror, new int[][][]{pattern}, keys);
        this.name = name;
    }

    public ResourceLocation getName() {
        return name;
    }

    @Override
    public SummoningCircle create(World world, MatchContext ctx) {
        SummoningCircle circle = new SummoningCircle(name, ctx.getPos(), ctx.getWidth(), ctx.getHeight());
        circle.setRotation(ctx.getRotation());
        if (ctx.isMirrored()) circle.mirror();
        return circle;
    }

}
