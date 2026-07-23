package net.smileycorp.magiadaemonica.common.demons;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.smileycorp.magiadaemonica.common.demons.contracts.Contract;
import net.smileycorp.magiadaemonica.common.demons.contracts.costs.Cost;
import net.smileycorp.magiadaemonica.common.demons.contracts.offerings.Offering;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class Demon {

    public static final DataSerializer<Demon> SERIALIZER = new Serializer();
    public static final Demon DEFAULT = new Demon("", Domain.PRIDE, Rank.LESSER_IMP);

    private final UUID uuid;
    private final String name;
    private final Domain domain;
    private final Rank rank;
    private int timesSummoned;
    private int timesTraded ;
    private List<Contract> customContracts = Lists.newArrayList();

    public Demon(String name, Domain domain, Rank rank) {
        this(name, domain, rank, 0, 0);
    }

    public Demon(String name, Domain domain, Rank rank, int timesSummoned, int timesTraded) {
        this.uuid = UUID.nameUUIDFromBytes(name.getBytes());
        this.name = name;
        this.domain = domain;
        this.rank = rank;
        this.timesSummoned = timesSummoned;
        this.timesTraded = timesTraded;
    }

    public void addCustomContract(Cost[] costs, Offering[] offerings) {
        customContracts.add(new Contract(uuid).addCosts(costs).addOfferings(offerings));
    }

    public boolean hasCustomContracts() {
        return !customContracts.isEmpty();
    }

    public List<Contract> getCustomContracts() {
        return customContracts;
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

    public ITextComponent getFormalName() {
        String key = "demon.magiadaemonica." + name.toLowerCase(Locale.US);
        return I18n.canTranslate(key) ? new TextComponentTranslation(key) : rank == null ? new TextComponentString(name) :
            domain == null ? new TextComponentTranslation("demon.magiadaemonica.unaligned.name", name,
                new TextComponentTranslation(rank.getTranslationKey()).getFormattedText()) :
                domain.getFormalName(name, rank);
    }

    public int getTimesSummoned() {
        return timesSummoned;
    }

    public void setTimesSummoned(int timesSummoned) {
        this.timesSummoned = timesSummoned;
    }

    public int getTimesTraded() {
        return timesTraded;
    }

    public void setTimesTraded(int timesTraded) {
        this.timesTraded = timesTraded;
    }

    @Override
    public String toString() {
        return name + (rank == null ? "" : ", " + (domain == null ? "unaligned " + rank : rank + " of " + domain));
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("name", name);
        if (domain != null) nbt.setString("domain", domain.getName());
        if (rank != null) nbt.setString("rank", rank.getName());
        nbt.setInteger("timesSummoned", timesSummoned);
        nbt.setInteger("timesTraded", timesTraded);
        if (!customContracts.isEmpty()) {
            NBTTagList customContracts = new NBTTagList();
            for (Contract contract : this.customContracts) customContracts.appendTag(contract.writeToNBT());
            nbt.setTag("customContracts", customContracts);
        }
        return nbt;
    }

    public static Demon fromNBT(NBTTagCompound nbt) {
        Demon demon = new Demon(nbt.getString("name"), nbt.hasKey("domain") ? Domain.get(nbt.getString("domain")) : null,
                nbt.hasKey("rank") ? Rank.get(nbt.getString("rank")) : null, nbt.hasKey("timesSummoned") ? nbt.getInteger("timesSummoned") : 0,
                nbt.hasKey("timesTraded") ? nbt.getInteger("timesTraded") : 0);
        if (nbt.hasKey("customContracts")) for (NBTBase nbt1 : nbt.getTagList("customContracts", 10))
            demon.customContracts.add(Contract.readFromNBT((NBTTagCompound) nbt1));
        return demon;
    }

    public static class Serializer implements DataSerializer<Demon> {

        @Override
        public void write(PacketBuffer buf, Demon demon) {
            ByteBufUtils.writeUTF8String(buf, demon.name);
            ByteBufUtils.writeUTF8String(buf, demon.domain == null ? "" : demon.domain.getName());
            ByteBufUtils.writeUTF8String(buf, demon.rank == null ? "" : demon.rank.getName());
            buf.writeInt(demon.timesSummoned);
            buf.writeInt(demon.timesTraded);
        }

        @Override
        public Demon read(PacketBuffer buf) throws IOException {
            return new Demon(ByteBufUtils.readUTF8String(buf), Domain.get(ByteBufUtils.readUTF8String(buf)),
                    Rank.get(ByteBufUtils.readUTF8String(buf)), buf.readInt(), buf.readInt());
        }

        @Override
        public DataParameter<Demon> createKey(int id) {
            return new DataParameter<>(id, this);
        }

        @Override
        public Demon copyValue(Demon demon) {
            return new Demon(demon.name, demon.domain, demon.rank, demon.timesSummoned, demon.timesTraded);
        }

    }

}
