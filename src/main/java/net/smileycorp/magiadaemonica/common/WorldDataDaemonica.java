package net.smileycorp.magiadaemonica.common;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldSavedData;
import net.smileycorp.magiadaemonica.common.demons.DemonRegistry;
import net.smileycorp.magiadaemonica.common.rituals.RitualsServer;

public class WorldDataDaemonica extends WorldSavedData {

    public static final String DATA = Constants.MODID;

    private WorldServer world;
    private final RitualsServer rituals = new RitualsServer(this);
    private final DemonRegistry demonRegistry = new DemonRegistry(this);

    public WorldDataDaemonica(String data) {
        super(data);
    }

    public RitualsServer getRituals() {
        return rituals;
    }

    public DemonRegistry getDemonRegistry() {
        return demonRegistry;
    }

    public WorldServer getWorld() {
        return world;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        rituals.readFromNBT(nbt);
        demonRegistry.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        rituals.writeToNBT(nbt);
        demonRegistry.writeToNBT(nbt);
        return nbt;
    }

    public static WorldDataDaemonica get(WorldServer world) {
        WorldDataDaemonica data = (WorldDataDaemonica) world.getMapStorage().getOrLoadData(WorldDataDaemonica.class, DATA);
        if (data == null) {
            data = new WorldDataDaemonica(DATA);
            world.getMapStorage().setData(DATA, data);
            data.getDemonRegistry().setup();
        }
        if (data.world == null) data.world = world;
        return data;
    }

}
