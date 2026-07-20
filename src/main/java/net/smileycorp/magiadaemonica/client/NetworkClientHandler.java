package net.smileycorp.magiadaemonica.client;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.smileycorp.atlas.api.data.Pair;
import net.smileycorp.magiadaemonica.client.gui.*;
import net.smileycorp.magiadaemonica.client.particle.ParticleFullbrightPixel;
import net.smileycorp.magiadaemonica.client.particle.ParticlePixel;
import net.smileycorp.magiadaemonica.common.EnumParticle;
import net.smileycorp.magiadaemonica.common.blocks.RitualBlock;
import net.smileycorp.magiadaemonica.common.blocks.tiles.RitualTile;
import net.smileycorp.magiadaemonica.common.blocks.tiles.TileRitualBasic;
import net.smileycorp.magiadaemonica.common.capabilities.Boons;
import net.smileycorp.magiadaemonica.common.capabilities.Curses;
import net.smileycorp.magiadaemonica.common.capabilities.DaemonicaCapabilities;
import net.smileycorp.magiadaemonica.common.demons.contracts.Contract;
import net.smileycorp.magiadaemonica.common.demons.contracts.ContractsUtils;
import net.smileycorp.magiadaemonica.common.entities.EntityContract;

import java.util.List;

public class NetworkClientHandler {
    
    private static final Minecraft mc = Minecraft.getMinecraft();

    public static void setSoul(float soul) {
        EntityPlayerSP player = mc.player;
        if (player == null) return;
        if (!player.hasCapability(DaemonicaCapabilities.SOUL, null)) return;
        player.getCapability(DaemonicaCapabilities.SOUL, null).setSoul(soul);
    }

    public static void openContractGUI(int id, Contract contract) {
        Entity entity = mc.world.getEntityByID(id);
        if (!(entity instanceof EntityContract)) return;
        mc.displayGuiScreen(new GuiContract((EntityContract)entity, contract));
    }

    public static void validateContract(int id) {
        Entity entity = mc.world.getEntityByID(id);
        if (!(entity instanceof EntityContract)) return;
        if (mc.currentScreen instanceof GuiContract) ((GuiContract) mc.currentScreen).validate(id);
    }

    public static void spawnParticle(EnumParticle type, double x, double y, double z, Double... data) {
        switch (type) {
            case PIXEL:
                mc.effectRenderer.addEffect(new ParticlePixel(mc.world, x, y, z, (int)(double)data[0], (int)(double)data[1], data[2], data[3], data[4], data[5]));
                break;
            case PIXEL_FULLBRIGHT:
                mc.effectRenderer.addEffect(new ParticleFullbrightPixel(mc.world, x, y, z, (int)(double)data[0], (int)(double)data[1], data[2], data[3], data[4], data[5]));
                break;
        }
    }

    public static void setCurses(List<Pair<ResourceLocation, Integer>> curses) {
        EntityPlayerSP player = mc.player;
        if (!player.hasCapability(DaemonicaCapabilities.CURSES, null)) return;
        Curses cap = player.getCapability(DaemonicaCapabilities.CURSES, null);
        for (Pair<ResourceLocation, Integer> pair : curses) cap.setLevel(pair.getFirst(), pair.getSecond());
    }

    public static void setBoons(List<Pair<ResourceLocation, Integer>> boons) {
        EntityPlayerSP player = mc.player;
        if (!player.hasCapability(DaemonicaCapabilities.BOONS, null)) return;
        Boons cap = player.getCapability(DaemonicaCapabilities.BOONS, null);
        for (Pair<ResourceLocation, Integer> pair : boons) cap.setLevel(pair.getFirst(), pair.getSecond());
    }

    public static void fillChat(String text) {
        GuiChat gui = new GuiChat();
        mc.displayGuiScreen(gui);
        gui.setText(text, true);
    }

    public static void openBlankScrollGUI(boolean mainhand) {
        mc.displayGuiScreen(new GuiBlankScroll(mainhand));
    }

    public static void openChoiceGui(GuiChoice gui) {
        GuiScreen screen = mc.currentScreen;
        if (screen instanceof GuiChoice) {
            ((GuiChooseBoonCurse) screen).queueScreen(gui);
            return;
        }
        mc.displayGuiScreen(gui);
    }

    public static void openChooseCurseBoonGUI(boolean isCurse, List<ResourceLocation> locs) {
        openChoiceGui(new GuiChooseBoonCurse(isCurse, locs));
    }

    public static void openChooseRelicGUI() {
        openChoiceGui(new GuiChooseItem(ContractsUtils.getRelics()));
    }

    public static void syncRitualTile(INetHandlerPlayClient netHandler, SPacketUpdateTileEntity packet) {
        BlockPos pos = packet.getPos();
        TileEntity tile = mc.world.getTileEntity(pos);
        if (!(tile instanceof RitualTile)) {
            IBlockState state = mc.world.getBlockState(pos);
            tile = state.getBlock() instanceof RitualBlock ?
                    ((RitualBlock) state.getBlock()).createRitualTile(mc.world, pos, state)
                    : new TileRitualBasic(pos);
            mc.world.setTileEntity(pos, tile);
        }
        tile.handleUpdateTag(packet.getNbtCompound());
    }

}
