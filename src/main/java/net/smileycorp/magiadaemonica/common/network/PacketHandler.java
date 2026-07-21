package net.smileycorp.magiadaemonica.common.network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.smileycorp.magiadaemonica.common.Constants;

public class PacketHandler {

	public static final SimpleNetworkWrapper NETWORK_INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MODID);

	public static void initPackets() {
		int id = 0;
		NETWORK_INSTANCE.registerMessage(DaemonicaParticleMessage::process, DaemonicaParticleMessage.class, id++, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(InvocationMessage::process, InvocationMessage.class, id++, Side.CLIENT);

		//rituals
		NETWORK_INSTANCE.registerMessage(SyncRitualMessage::process, SyncRitualMessage.class, id++, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(RemoveRitualMessage::process, RemoveRitualMessage.class, id++, Side.CLIENT);

		//contracts
		NETWORK_INSTANCE.registerMessage(OpenContractMessage::process, OpenContractMessage.class, id++, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(SignContractMessage::process, SignContractMessage.class, id++, Side.SERVER);
		NETWORK_INSTANCE.registerMessage(ValidateContractMessage::process, ValidateContractMessage.class, id++, Side.CLIENT);

		//scrolls
		NETWORK_INSTANCE.registerMessage(BlankScrollMessage::process, BlankScrollMessage.class, id++, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(InscribeScrollMessage::process, InscribeScrollMessage.class, id++, Side.SERVER);
		NETWORK_INSTANCE.registerMessage(FillChatMessage::process, FillChatMessage.class, id++, Side.CLIENT);

		//curse boons
		NETWORK_INSTANCE.registerMessage(ChooseCurseBoonMessage::process, ChooseCurseBoonMessage.class, id++, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(AddCurseBoonMessage::process, AddCurseBoonMessage.class, id++, Side.SERVER);

		//relics
		NETWORK_INSTANCE.registerMessage(ChooseRelicMessage::process, ChooseRelicMessage.class, id++, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(AddItemMessage::process, AddItemMessage.class, id++, Side.SERVER);

		//data sync messages
		NETWORK_INSTANCE.registerMessage(SyncSoulMessage::process, SyncSoulMessage.class, id++, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(SyncCursesMessage::process, SyncCursesMessage.class, id++, Side.CLIENT);
		NETWORK_INSTANCE.registerMessage(SyncBoonsMessage::process, SyncBoonsMessage.class, id++, Side.CLIENT);
	}

}
