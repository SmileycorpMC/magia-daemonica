package net.smileycorp.magiadaemonica.common.demons;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.io.IOException;
import java.util.UUID;

public class Demon {

    public static final DataSerializer<Demon> SERIALIZER = new Serializer();
    public static final Demon DEFAULT = new Demon("", Domain.PRIDE, Rank.LESSER_IMP);

    private final UUID uuid;
    private final String name;
    private final Domain domain;
    private final Rank rank;

    public Demon(String name, Domain domain, Rank rank) {
        this.uuid = UUID.nameUUIDFromBytes(name.getBytes());
        this.name = name;
        this.domain = domain;
        this.rank = rank;
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public Domain getDomain() {
        return domain;
    }

    public Rank getRank() {
        return rank;
    }

    public boolean isPersistent() {
        return rank.isPersistent();
    }

    @Override
    public String toString() {
        return name + ", " + rank + " of " + domain;
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("name", name);
        nbt.setString("domain", domain.getName());
        nbt.setString("rank", rank.getName());
        return nbt;
    }

    public static Demon fromNBT(NBTTagCompound nbt) {
        return new Demon(nbt.getString("name"), Domain.get(nbt.getString("domain")),
                Rank.get(nbt.getString("rank")));
    }

    public static class Serializer implements DataSerializer<Demon> {

        @Override
        public void write(PacketBuffer buf, Demon demon) {
            ByteBufUtils.writeUTF8String(buf, demon.name);
            ByteBufUtils.writeUTF8String(buf, demon.domain.getName());
            ByteBufUtils.writeUTF8String(buf, demon.rank.getName());
        }

        @Override
        public Demon read(PacketBuffer buf) throws IOException {
            return new Demon(ByteBufUtils.readUTF8String(buf), Domain.get(ByteBufUtils.readUTF8String(buf)),
                    Rank.get(ByteBufUtils.readUTF8String(buf)));
        }

        @Override
        public DataParameter<Demon> createKey(int id) {
            return new DataParameter<>(id, this);
        }

        @Override
        public Demon copyValue(Demon demon) {
            return new Demon(demon.name, demon.domain, demon.rank);
        }

    }

}
