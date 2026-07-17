package net.smileycorp.magiadaemonica.client;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.Random;

public class DaemonicaItemProperties {

    private static final Minecraft mc = Minecraft.getMinecraft();

    public static int aleaDiabolaNumber(ItemStack stack, World world, EntityLivingBase entity)  {
        NBTTagCompound nbt = stack.getTagCompound();
        if (nbt == null) return 20;
        if (nbt.hasKey("roll_ticks") && nbt.getByte("roll_ticks") > 20) return new Random(mc.world.getWorldTime()).nextInt(20);
        return nbt.hasKey("number") ? nbt.getByte("number") : 20;
    }


}
