package net.smileycorp.magiadaemonica.common.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class DaemonicaCapabilities {

	@CapabilityInject(Soul.class)
	public final static Capability<Soul> SOUL = null;

	@CapabilityInject(Contracts.class)
	public final static Capability<Contracts> CONTRACTS = null;

	@CapabilityInject(Affiliation.class)
	public final static Capability<Affiliation> AFFILIATION = null;

	@CapabilityInject(Curses.class)
	public final static Capability<Curses> CURSES = null;
	
}
