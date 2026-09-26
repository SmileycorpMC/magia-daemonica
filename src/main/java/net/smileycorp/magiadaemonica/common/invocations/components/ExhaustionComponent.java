package net.smileycorp.magiadaemonica.common.invocations.components;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.smileycorp.magiadaemonica.common.invocations.InvocationContext;

public class ExhaustionComponent implements MagiaComponent {

    private final float exhaustion;

    public ExhaustionComponent(float exhaustion) {
       this.exhaustion = exhaustion;
    }

    @Override
    public boolean canApply(InvocationContext ctx) {
        EntityPlayer player = ctx.getPlayer();
        FoodStats stats = player.getFoodStats();
        return player.capabilities.disableDamage || (int) exhaustion <= (int) (4 * (stats.getFoodLevel() + stats.getSaturationLevel())
                + stats.foodExhaustionLevel);
    }

    @Override
    public void consumeComponent(InvocationContext ctx) {
        EntityPlayer player = ctx.getPlayer();
        if (player.capabilities.disableDamage) return;
        ctx.getPlayer().addExhaustion(exhaustion);
    }

    @Override
    public ITextComponent getDescription() {
        return new TextComponentTranslation("invocation.magiadaemonica.component.exhaustion", exhaustion);
    }

}
