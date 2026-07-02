package net.smileycorp.magiadaemonica.mixin;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.smileycorp.magiadaemonica.common.capabilities.Curses;
import net.smileycorp.magiadaemonica.common.demons.contracts.CursesRegistry;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(ItemStack.class)
public class MixinItemStack {

    @WrapMethod(method = "attemptDamageItem")
    private boolean magiadaemonica$attemptDamageItem(int amount, Random rand, EntityPlayerMP damager, Operation<Boolean> original) {
        if (damager != null) {
            int corrosion = Curses.getLevel(damager, CursesRegistry.CORROSION);
            if (corrosion > 0) amount *= (int) Math.pow(2, corrosion);
        }
        return original.call(amount, rand, damager);
    }

}
