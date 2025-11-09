package net.smileycorp.magiadaemonica.common;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.smileycorp.magiadaemonica.common.capabilities.ISoul;

public class MagiaDaemonicaCapabilities {

	@CapabilityInject(ISoul.class)
	public final static Capability<ISoul> SOUL = null;
	
}
