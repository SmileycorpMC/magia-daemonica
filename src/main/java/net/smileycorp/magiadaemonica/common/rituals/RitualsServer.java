package net.smileycorp.magiadaemonica.common.rituals;

import com.google.common.collect.Maps;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.smileycorp.magiadaemonica.common.WorldDataDaemonica;
import net.smileycorp.magiadaemonica.common.network.PacketHandler;
import net.smileycorp.magiadaemonica.common.network.RemoveRitualMessage;
import net.smileycorp.magiadaemonica.common.network.SyncRitualMessage;

import java.util.Collection;
import java.util.Map;

public class RitualsServer implements Rituals {

    private final Map<BlockPos, Ritual> rituals = Maps.newHashMap();
    private final WorldDataDaemonica data;
    private WorldServer world;

    public RitualsServer(WorldDataDaemonica data) {
        this.data = data;
    }

    @Override
    public Ritual getRitual(BlockPos pos) {
        Ritual ritual = rituals.get(pos);
        if (ritual != null) return ritual;
        return findRitual(pos);
    }

    @Override
    public Ritual getRitual(double x, double y, double z, double range) {
        double rangeSqr = range * range;
        for (Ritual ritual : rituals.values()) if (ritual.getCenterPos().distanceSqToCenter(x, y, z) <= rangeSqr) return ritual;
        return null;
    }

    private Ritual findRitual(BlockPos pos) {
        for (Ritual ritual : rituals.values()) if (ritual.contains(pos)) return ritual;
        return null;
    }

    @Override
    public void addRitual(Ritual ritual) {
        rituals.put(ritual.getCenterPos(), ritual);
        syncRitual(ritual);
        data.markDirty();
    }

    @Override
    public void removeRitual(BlockPos pos) {
        Ritual ritual = getRitual(pos);
        if (ritual == null) return;
        ritual.remove(world);
        pos = ritual.getCenterPos();
        rituals.remove(pos);
        PacketHandler.NETWORK_INSTANCE.sendToAllTracking(new RemoveRitualMessage(pos),
                new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(),
                        128));
        data.markDirty();
    }

    @Override
    public void tick() {
        if (world == null) return;
        for (Ritual ritual : rituals.values()) {
            ritual.tick(world);
            if (ritual.isDirty()) {
                data.markDirty();
                syncRitual(ritual);
            }
        }
    }

    @Override
    public Collection<Ritual> getRituals() {
        return rituals.values();
    }

    public void syncRitual(Ritual ritual) {
        BlockPos pos = ritual.getCenterPos();
        PacketHandler.NETWORK_INSTANCE.sendToAllTracking(new SyncRitualMessage(pos, ritual),
                new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), 128));
        ritual.markDirty(false);
    }

    public void syncRituals(Chunk chunk) {
        World world = chunk.getWorld();
        for (Ritual ritual : rituals.values()) {
            BlockPos pos = ritual.getCenterPos();
            if (world.getChunkFromBlockCoords(pos) == chunk) syncRitual(ritual);
        }
    }

    public WorldServer getWorld() {
        return world;
    }

    public void setWorld(WorldServer world) {
        this.world = world;
    }

    public void readFromNBT(NBTTagList nbt) {
        for (NBTBase tag : nbt) {
            Ritual ritual = RitualsRegistry.getRitualFromNBT((NBTTagCompound) tag);
            if (ritual != null) rituals.put(ritual.getCenterPos(), ritual);
        }
    }

    public NBTTagList writeToNBT() {
        NBTTagList nbt = new NBTTagList();
        for (Ritual ritual : rituals.values()) {
            NBTTagCompound tag = ritual.writeToNBT();
            tag.setString("id", ritual.getID().toString());
            nbt.appendTag(tag);
        }
        return nbt;
    }

    public static RitualsServer get(WorldServer world) {
        return WorldDataDaemonica.get().getRituals(world);
    }
    
}
