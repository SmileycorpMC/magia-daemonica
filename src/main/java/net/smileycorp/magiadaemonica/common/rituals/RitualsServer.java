package net.smileycorp.magiadaemonica.common.rituals;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.smileycorp.magiadaemonica.common.WorldDataDaemonica;
import net.smileycorp.magiadaemonica.common.blocks.tiles.TileRitualBasic;
import net.smileycorp.magiadaemonica.common.network.PacketHandler;
import net.smileycorp.magiadaemonica.common.network.RemoveRitualMessage;
import net.smileycorp.magiadaemonica.common.network.SyncRitualMessage;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class RitualsServer implements Rituals {

    private final Map<BlockPos, Ritual> rituals = Maps.newHashMap();
    private final WorldDataDaemonica data;
    private WorldServer world;
    private List<BlockPos> toRemove = Lists.newArrayList();
    private List<Ritual> toAdd = Lists.newArrayList();
    private boolean ticking;

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
        if (ticking) toAdd.add(ritual);
        rituals.put(ritual.getCenterPos(), ritual);
        syncRitual(ritual);
        data.markDirty();
    }

    @Override
    public void removeRitual(BlockPos pos) {
        if (ticking) {
            toRemove.add(pos);
            return;
        }
        Ritual ritual = getRitual(pos);
        if (ritual == null) return;
        pos = ritual.getCenterPos();
        rituals.remove(pos);
        ritual.remove(world);
        PacketHandler.NETWORK_INSTANCE.sendToAllTracking(new RemoveRitualMessage(pos),
                new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(),
                        128));
        data.markDirty();
    }

    @Override
    public void tick() {
        ticking = true;
        if (world == null) return;
        for (Ritual ritual : Lists.newArrayList(rituals.values())) {
            ritual.tick(world);
            if (ritual.isDirty()) {
                data.markDirty();
                syncRitual(ritual);
            }
        }
        ticking = false;
        toAdd.forEach(this::addRitual);
        toAdd.clear();
        toRemove.forEach(this::removeRitual);
        toRemove.clear();
    }

    @Override
    public Collection<Ritual> getRituals() {
        return rituals.values();
    }

    public void syncRitual(Ritual ritual) {
        BlockPos center = ritual.getCenterPos();
        PacketHandler.NETWORK_INSTANCE.sendToAllTracking(new SyncRitualMessage(center, ritual),
                new NetworkRegistry.TargetPoint(world.provider.getDimension(), center.getX(), center.getY(), center.getZ(), 128));
        ritual.markDirty(false);
        BlockPos pos = ritual.getPos();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int i = 0; i < ritual.getWidth(); i++) for (int k = 0; k < ritual.getHeight(); k++) {
            mutable.setPos(pos.getX() + i, pos.getY(), pos.getZ() + k);
            TileEntity tile = world.getChunkFromBlockCoords(mutable).getTileEntity(mutable, Chunk.EnumCreateEntityType.CHECK);
            if (!(tile instanceof TileRitualBasic)) continue;
            ((TileRitualBasic) tile).forceUpdate();
        }
    }

    public void syncRituals(Chunk chunk) {
        for (Ritual ritual : rituals.values()) {
            BlockPos center = ritual.getCenterPos();
            if (!chunk.isAtLocation(center.getX() >> 4, center.getZ() >> 4)) syncRitual(ritual);
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
