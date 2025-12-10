package net.smileycorp.magiadaemonica.common;

import com.google.common.collect.Maps;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.smileycorp.magiadaemonica.common.demons.DemonRegistry;
import net.smileycorp.magiadaemonica.common.rituals.RitualsServer;

import java.util.Map;

public class WorldDataDaemonica extends WorldSavedData {

    public static final String DATA = Constants.MODID;

    private final Map<Integer, RitualsServer> rituals = Maps.newHashMap();
    private final DemonRegistry demonRegistry = new DemonRegistry(this);

    public WorldDataDaemonica(String data) {
        super(data);
    }

    public RitualsServer getRituals(WorldServer world) {
        RitualsServer rituals = this.rituals.computeIfAbsent(world.provider.getDimension(), id -> new RitualsServer(this));
        if (rituals.getWorld() != world) rituals.setWorld(world);
        return rituals;
    }

    public DemonRegistry getDemonRegistry() {
        return demonRegistry;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        if (nbt.hasKey("rituals")) {
            NBTTagCompound compound = nbt.getCompoundTag("rituals");
            for (String key : compound.getKeySet()) {
                RitualsServer rituals = new RitualsServer(this);
                rituals.readFromNBT(compound.getTagList(key, 10));
                this.rituals.put(Integer.parseInt(key), rituals);
            }
        }
        demonRegistry.readFromNBT(nbt);
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
        NBTTagCompound rituals = new NBTTagCompound();
        for (Map.Entry<Integer, RitualsServer> entry : this.rituals.entrySet())
            rituals.setTag(entry.getKey().toString(), entry.getValue().writeToNBT());
        nbt.setTag("rituals", rituals);
        demonRegistry.writeToNBT(nbt);
        return nbt;
    }

    public static WorldDataDaemonica get() {
        WorldServer world = FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0);
        WorldDataDaemonica data = (WorldDataDaemonica) world.getMapStorage().getOrLoadData(WorldDataDaemonica.class, DATA);
        if (data == null) {
            data = new WorldDataDaemonica(DATA);
            world.getMapStorage().setData(DATA, data);
            data.getDemonRegistry().setup();
        }
        return data;
    }

}
