package net.smileycorp.magiadaemonica.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.FoodStats;
import net.smileycorp.magiadaemonica.common.capabilities.Curses;
import net.smileycorp.magiadaemonica.common.demons.contracts.CursesRegistry;
import net.smileycorp.magiadaemonica.common.util.FoodStatsPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(FoodStats.class)
public class MixinFoodStats implements FoodStatsPlayer {

    @Unique
    private EntityPlayer player;

    @Override
    public void setPlayer(EntityPlayer player) {
        this.player = player;
    }

    @Override
    public EntityPlayer getPlayer() {
        return player;
    }

    @WrapMethod(method = "addStats(IF)V")
    private void magiadaemonica$modifyFoodStats(int hunger, float saturation, Operation<Void> original) {
        int voracity = Curses.getLevel(player, CursesRegistry.VORACITY);
        if (voracity > 0) hunger /= (voracity + 1);
        original.call(hunger, saturation);
    }

}
