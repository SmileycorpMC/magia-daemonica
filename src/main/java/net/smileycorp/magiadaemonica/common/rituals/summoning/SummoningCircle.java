package net.smileycorp.magiadaemonica.common.rituals.summoning;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.blocks.BlockChalkLine;
import net.smileycorp.magiadaemonica.common.blocks.DaemonicaBlocks;
import net.smileycorp.magiadaemonica.common.demons.DemonRegistry;
import net.smileycorp.magiadaemonica.common.demons.contracts.ContractsUtils;
import net.smileycorp.magiadaemonica.common.entities.EntityAbstractDemon;
import net.smileycorp.magiadaemonica.common.entities.EntityContract;
import net.smileycorp.magiadaemonica.common.entities.EntityDemonicTrader;
import net.smileycorp.magiadaemonica.common.potions.DaemonicaPotions;
import net.smileycorp.magiadaemonica.common.rituals.Ritual;
import net.smileycorp.magiadaemonica.common.rituals.Rotation;

import java.util.Optional;
import java.util.Random;
import java.util.UUID;

public class SummoningCircle implements Ritual {

    public static final ResourceLocation ID = Constants.loc("summoning_circle");

    private final BlockPos pos;
    private final int width, height;
    private final Vec3d center;
    private final ResourceLocation name;
    private final float[][] candles;
    private boolean mirror;
    private Rotation rotation = Rotation.NORTH;
    private boolean isDirty;
    private int power, lastPower = 0;
    private boolean active = false;
    private int ticksActive;
    private EntityDemonicTrader demon;
    private UUID playerUUID;
    private EntityPlayer player;

    public SummoningCircle(ResourceLocation name, BlockPos pos, int width, int height) {
        this.pos = pos;
        this.width = width;
        this.height = height;
        this.center = new Vec3d(pos.getX() + width  * 0.5f, pos.getY(), pos.getZ() + height  * 0.5f);
        this.name = name;
        this.candles = SummoningCircles.getCandles(name);
    }

    public void setBlocks(World world, BlockChalkLine.RitualState ritualState) {
        BlockPos.MutableBlockPos mutable;
        for (int x = 0; x < width; x++) {
            for (int z = 0; z < height; z++) {
                mutable = new BlockPos.MutableBlockPos(pos.getX() + x, pos.getY(), pos.getZ() + z);
                IBlockState state = world.getBlockState(mutable);
                if (state.getBlock() != DaemonicaBlocks.CHALK_LINE) continue;
                world.setBlockState(mutable, state.withProperty(BlockChalkLine.RITUAL_STATE, ritualState));
            }
        }
    }

    @Override
    public void remove(World world) {
       setBlocks(world, BlockChalkLine.RitualState.NONE);
       if (demon != null) demon.setPose(EntityDemonicTrader.Pose.DESPAWNING);
       for (EntityContract contract : world.getEntitiesWithinAABB(EntityContract.class,
               new AxisAlignedBB(getCenterPos()).grow(width, 10, height))) contract.setDead();
    }

    @Override
    public boolean isDirty() {
        return isDirty;
    }

    @Override
    public void markDirty(boolean dirty) {
        isDirty = dirty;
    }

    public void mirror() {
        this.mirror = true;
    }

    public void setRotation(Rotation rotation) {
        this.rotation = rotation;
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }

    public ResourceLocation getName() {
        return name;
    }

    @Override
    public BlockPos getPos() {
        return pos;
    }

    @Override
    public Vec3d getCenter() {
        return center;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    public float[][] getCandles() {
        return candles;
    }

    @Override
    public Rotation getRotation() {
        return rotation;
    }

    @Override
    public boolean isMirrored() {
        return mirror;
    }

    @Override
    public void tick(World world) {
        if (world.isRemote) {
            lastPower = power;
            Random rand = world.rand;
            for (float[] candle : candles) {
                candle = rotation.apply(candle);
                if (mirror) for (int i = 0; i < 2; i++) candle[i] = -candle[i];
                IBlockState state = world.getBlockState(new BlockPos(center.addVector(candle[0], 0, candle[1])));
                if (state.getBlock() != DaemonicaBlocks.CHALK_LINE) continue;
                if (state.getValue(BlockChalkLine.CANDLE) != BlockChalkLine.Candle.LIT) continue;
                world.spawnParticle(EnumParticleTypes.FLAME, center.x + candle[0], center.y + 0.6, center.z + candle[1], 0, 0, 0);
                if (rand.nextInt(4) == 0)
                    world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, center.x + candle[0] + (rand.nextFloat() - 0.5) * 0.05,
                            center.y + 0.6, center.z + candle[1] + (rand.nextFloat() - 0.5) * 0.05, 0, 0, 0);
            }
            if (ticksActive > 80 && ticksActive < 200) spawnParticles(world, EnumParticleTypes.SMOKE_NORMAL);
            if (ticksActive > 200) spawnParticles(world, EnumParticleTypes.FLAME);
            return;
        }
        if (!active) return;
        //if (ticksActive == 3) world.setBlockState(new BlockPos(center), Blocks.FIRE.getDefaultState());
        if (ticksActive == 80) {
            world.setBlockState(new BlockPos(center), Blocks.AIR.getDefaultState());
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos);
            for (float[] candle : candles) {
                candle = rotation.apply(candle);
                if (mirror) for (int i = 0; i < 2; i++) candle[i] = -candle[i];
                mutable.setPos(center.x + candle[0], center.y, center.z + candle[1]);
                world.setBlockState(mutable, world.getBlockState(mutable).withProperty(BlockChalkLine.CANDLE, BlockChalkLine.Candle.UNLIT));
            }
            world.playSound(null, center.x, center.y, center.z, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.HOSTILE, 0.75f, 1);
            for (EntityPlayerMP player : world.getPlayers(EntityPlayerMP.class, player -> player.getDistanceSq(center.x, center.y, center.z) <= 256))
                player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 140, 4, true, false));
        }
        if (ticksActive == 200) {
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos(pos);
            for (float[] candle : candles) {
                candle = rotation.apply(candle);
                if (mirror) for (int i = 0; i < 2; i++) candle[i] = -candle[i];
                mutable.setPos(center.x + candle[0], center.y, center.z + candle[1]);
                world.setBlockState(mutable, world.getBlockState(mutable).withProperty(BlockChalkLine.CANDLE, BlockChalkLine.Candle.LIT));
            }
        }
        if (ticksActive == 250) for (EntityPlayerMP player : world.getPlayers(EntityPlayerMP.class, player -> player.getDistanceSq(center.x, center.y, center.z) <= 256))
            player.addPotionEffect(new PotionEffect(DaemonicaPotions.TREMOR, 460, 0, true, false));
        if (ticksActive > 200 && ticksActive < 320 && ticksActive % 40 == 0)
            world.playSound(null, center.x, center.y, center.z, SoundEvents.ENTITY_GUARDIAN_ATTACK, SoundCategory.HOSTILE, 0.75f, 1);
        if (ticksActive >= 320 && ticksActive <= 560  && ticksActive % 40 == 0)
            world.playSound(null, center.x, center.y, center.z, SoundEvents.ENTITY_WITHER_BREAK_BLOCK, SoundCategory.HOSTILE, 0.75f, 1);
        if (ticksActive == 600) {
            demon = new EntityDemonicTrader(world);
            demon.setDemon(DemonRegistry.get((WorldServer) world).create(world.rand, power));
            demon.setPose(EntityDemonicTrader.Pose.SUMMONING);
            demon.setPosition(center.x, center.y, center.z);
            demon.setRitual(getCenterPos());
            demon.getLookHelper().setLookPositionWithEntity(player, 0, 0);
            world.spawnEntity(demon);
        }
        if (ticksActive == 680) {
            int count = ContractsUtils.getContractCount(demon.getDemon(), player);
            double angle = Math.atan2(player.posZ - center.z, player.posX - center.x);
            double step = Math.PI * 0.25f / ((float)count * 0.75f);
            angle -= step * (count -1) * 0.5;
            for (int i = 0 ; i < count; i++) {
                EntityContract contract = new EntityContract(world);
                contract.setPosition(center.x + Math.cos(angle) * 2, center.y + 1, center.z + Math.sin(angle) * 2);
                angle += step;
                world.spawnEntity(contract);
            }
        }
        ticksActive++;
        isDirty = true;
    }

    private void spawnParticles(World world, EnumParticleTypes type) {
        Random rand = world.rand;
        int r = width / 2;
        for (int i = 0; i < rand.nextInt(3); i++) {
            Vec3d dir = DirectionUtils.getRandomDirectionVecXZ(world.rand);
            world.spawnParticle(type, center.x + dir.x * r, center.y + 0.05, center.z + dir.z * r, 0, 0, 0);
        }
        double innerRad = 0;
        double mult = 1;
        if (ticksActive >= 560) {
            innerRad = 1;
            mult = 20;
        }
        else if (ticksActive >= 520) {
            innerRad = 0.3;
            mult = 5;
        }
        for (int i = 0; i < rand.nextInt(3) + 2; i++) {
            Vec3d dir = DirectionUtils.getRandomDirectionVecXZ(world.rand);
            float magnitude = rand.nextFloat() * r;
            double speed = 0.01 * (r - magnitude);
            if (magnitude < innerRad) speed *= mult;
            world.spawnParticle(type, center.x + dir.x * magnitude, center.y + 0.05, center.z + dir.z * magnitude, 0, speed, 0);
        }
    }

    @Override
    public void addPower(int power) {
        this.power += power;
        if (this.power > 2000) this.power = 2000;
        isDirty = true;
    }

    @Override
    public int getPower() {
        return power;
    }

    public int getLastTickPower() {
        return lastPower;
    }

    public int getTicksActive() {
        return ticksActive;
    }

    public Optional<EntityPlayer> getPlayer() {
        if (playerUUID == null) return Optional.empty();
        if (player == null) player = FMLCommonHandler.instance().getMinecraftServerInstance()
                .getPlayerList().getPlayerByUUID(playerUUID);
        return player == null ? Optional.empty() : Optional.of(player);
    }

    @Override
    public void setPlayer(UUID player) {
        playerUUID = player;
        this.player = FMLCommonHandler.instance().getMinecraftServerInstance()
                .getPlayerList().getPlayerByUUID(player);
    }

    @Override
    public void setDemon(EntityAbstractDemon demon) {
        if (!(demon instanceof EntityDemonicTrader)) return;
        this.demon = (EntityDemonicTrader) demon;
    }

    @Override
    public EntityAbstractDemon getDemon() {
        return demon;
    }

    @Override
    public NBTTagCompound writeToNBT() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setTag("pos", NBTUtil.createPosTag(pos));
        nbt.setInteger("width", width);
        nbt.setInteger("height", height);
        nbt.setString("name", name.toString());
        nbt.setByte("rotation", (byte) rotation.ordinal());
        nbt.setBoolean("mirror", mirror);
        nbt.setBoolean("active", active);
        nbt.setInteger("power", power);
        nbt.setInteger("ticksActive", ticksActive);
        if (playerUUID != null) nbt.setUniqueId("player", playerUUID);
        return nbt;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        rotation = Rotation.values()[nbt.getByte("rotation")];
        mirror = nbt.getBoolean("mirror");
        active = nbt.getBoolean("active");
        power = nbt.getInteger("power");
        ticksActive = nbt.getInteger("ticksActive");
        if (nbt.hasKey("player")) setPlayer(nbt.getUniqueId("player"));
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public boolean canPower() {
        return !active && power < 2000;
    }

    @Override
    public void processInvocation(EntityPlayer player, String invocation) {
        if (active || power < 500) return;
        World world = player.world;
        if (world.isDaytime()) return;
        if (!"te infernale invoco pacisci volo".equals(invocation)) return;
        for (float[] candle : candles) {
            candle = rotation.apply(candle);
            if (mirror) for (int i = 0; i < 2; i++) candle[i] = -candle[i];
            IBlockState state = world.getBlockState(new BlockPos(center.addVector(candle[0], 0, candle[1])));
            if (state.getBlock() != DaemonicaBlocks.CHALK_LINE) return;
            if (state.getValue(BlockChalkLine.CANDLE) != BlockChalkLine.Candle.LIT) return;
        }
        EntityLightningBolt bolt = new EntityLightningBolt(world, center.x, center.y, center.z, false);
        world.spawnEntity(bolt);
        setBlocks(world, BlockChalkLine.RitualState.ACTIVE);
        active = true;
        this.player = player;
        playerUUID = player.getUniqueID();
    }

    public static SummoningCircle fromNBT(NBTTagCompound nbt) {
        SummoningCircle circle = new SummoningCircle(new ResourceLocation(nbt.getString("name")),
                NBTUtil.getPosFromTag(nbt.getCompoundTag("pos")), nbt.getInteger("width"), nbt.getInteger("height"));
        circle.readFromNBT(nbt);
        return circle;
    }

}
