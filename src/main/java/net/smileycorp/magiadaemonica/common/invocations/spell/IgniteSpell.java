package net.smileycorp.magiadaemonica.common.invocations.spell;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.oredict.OreIngredient;
import net.smileycorp.atlas.api.util.DirectionUtils;
import net.smileycorp.magiadaemonica.common.blocks.Lightable;
import net.smileycorp.magiadaemonica.common.invocations.MateriaInvocation;

import java.util.List;

public class IgniteSpell extends MateriaInvocation {

    public IgniteSpell() {
        super(new OreIngredient("ashOak"));
    }

    @Override
    protected boolean tryCast(EntityPlayer player) {
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
            for (BlockPos pos1 : litBlocks) {
                Vec3d dir = DirectionUtils.getDirectionVecXZ(new Vec3d(player.posX, player.posY, player.posZ), new Vec3d(pos1));
                ((WorldServer)world).spawnParticle(EnumParticleTypes.FLAME, player.posX, player.posY + 0.5f, player.posZ,
                        0, dir.x, dir.y, dir.z, Math.sqrt(player.getDistanceSqToCenter(pos1)) * 0.25);
            }
        }
        return lit;
    }

}
