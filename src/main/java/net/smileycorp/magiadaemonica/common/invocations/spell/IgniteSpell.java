package net.smileycorp.magiadaemonica.common.invocations.spell;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreIngredient;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.magiadaemonica.client.ClientUtils;
import net.smileycorp.magiadaemonica.common.blocks.Lightable;
import net.smileycorp.magiadaemonica.common.invocations.Invocation;
import net.smileycorp.magiadaemonica.common.invocations.InvocationContext;
import net.smileycorp.magiadaemonica.common.invocations.components.MateriaComponent;
import net.smileycorp.magiadaemonica.common.invocations.components.VocalisComponent;

import java.util.List;

public class IgniteSpell extends Invocation implements Invocation.ClientInvocation {

    public IgniteSpell() {
        super();
        addComponent(new VocalisComponent("scintilla in ignem"));
        addComponent(new MateriaComponent(new OreIngredient("ashOak")));
    }

    @Override
    public Invocation.InvocationResult apply(InvocationContext ctx) {
        EntityPlayer player = ctx.getPlayer();
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(player.getPosition());
        World world = player.world;
        IBlockState state;
        List<BlockPos> litBlocks = Lists.newArrayList();
        for (int i = -4; i < 5; i++) {
            for (int j = -2; j < 3; j++) {
                for (int k = -4; k < 5; k++) {
                    pos.setPos(player.posX + i, player.posY + j, player.posZ + k);
                    state = world.getBlockState(pos);
                    if (!(state.getBlock() instanceof Lightable)) continue;
                    Lightable lightable = (Lightable) state.getBlock();
                    if (!lightable.isLightable(world, pos, state)) continue;
                    lightable.light(world, pos, state);
                    litBlocks.add(pos);
                }
            }
        }
        boolean lit = !litBlocks.isEmpty();
        if (lit) {
            world.playSound(null, player.posX, player.posY + 0.5f, player.posZ, SoundEvents.ITEM_FIRECHARGE_USE,
                    player.getSoundCategory(), 0.75f, player.getRNG().nextFloat() * 0.5f - 0.25f);
            List<Object> args = Lists.newArrayListWithExpectedSize(litBlocks.size() + 1);
            args.add(ctx.hasFlag(MateriaComponent.OFFHAND_FLAG));
            args.addAll(litBlocks);
            return InvocationResult.withArgs(args.toArray(args.toArray(new Object[args.size()])));
        }
        return null;
    }

    @Override
    public void writeToBuf(ByteBuf buf, Object... args) {
        buf.writeBoolean((boolean) args[0]);
        for (int i = 1; i < args.length; i++) {
            BlockPos pos = (BlockPos) args[i];
            buf.writeInt(pos.getX());
            buf.writeInt(pos.getY());
            buf.writeInt(pos.getZ());
        }
    }

    @Override
    public Object[] readFromBuf(ByteBuf buf) {
        List<Object> args = Lists.newArrayList();
        args.add(buf.readBoolean());
        while (buf.isReadable()) args.add(new BlockPos(buf.readInt(), buf.readInt(), buf.readInt()));
        return args.toArray(new Object[args.size()]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void packetReceived(EntityPlayer player, Object... args) {
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayerSP localPlayer = mc.player;
        World world = localPlayer.world;
        EnumHand hand = (boolean) args[0] ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
        Vec3d origin = player == localPlayer && mc.gameSettings.thirdPersonView == 0 ? ClientUtils.getHandPosition(hand)
                : player.getPositionEyes(1);
        for (int i = 1; i < args.length; i++) {
            BlockPos pos = (BlockPos) args[i];
            Vec3d dir = DirectionUtils.getDirectionVec(origin, DirectionUtils.centerOf(pos));
            double dx = pos.getX() + 0.5 - origin.x;
            double dy = pos.getY() + 0.5 - origin.y;
            double dz = pos.getZ() + 0.5 - origin.z;
            double speed = Math.sqrt(dx * dx + dy * dy + dz * dz) * 0.5;
            world.spawnParticle(EnumParticleTypes.FLAME, origin.x, origin.y, origin.z, dir.x * speed, dir.y * speed, dir.z * speed);
        }
    }

}
