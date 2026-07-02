package net.smileycorp.magiadaemonica.common.damage;

import net.minecraft.util.DamageSource;

public class DaemonicaDamageSources {

    public static final DamageSource BLEED = new DamageSource("bleed").setDamageIsAbsolute().setDamageBypassesArmor();
    public static final DamageSource KNIFESTEP = new DamageSource("knifestep").setDamageIsAbsolute().setDamageBypassesArmor();

}
