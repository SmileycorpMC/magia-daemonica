package net.smileycorp.magiadaemonica.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.Vec3d;
import net.smileycorp.atlas.api.util.DirectionUtils;

public class ClientUtils {

    public static void renderPlane(double x1, double y1, double z1, double x2, double y2, double z2, float r, float g, float b, float a, boolean doubleFaced) {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR_NORMAL);
        double dy = (y1 + y2) * 0.5;
        buffer.pos(x1, y1, z1).tex(0, 0).color(r, g, b, a).normal(0, 1, 0).endVertex();
        buffer.pos(x1, dy, z2).tex(0, 1).color(r, g, b, a).normal(0, 1, 0).endVertex();
        buffer.pos(x2, y2, z2).tex(1, 1).color(r, g, b, a).normal(0, 1, 0).endVertex();
        buffer.pos(x2, dy, z1).tex(1, 0).color(r, g, b, a).normal(0, 1, 0).endVertex();
        if (doubleFaced) {
            buffer.pos(x2, dy, z1).tex(1, 0).color(r, g, b, a).normal(0, 1, 0).endVertex();
            buffer.pos(x2, y2, z2).tex(1, 1).color(r, g, b, a).normal(0, 1, 0).endVertex();
            buffer.pos(x1, dy, z2).tex(0, 1).color(r, g, b, a).normal(0, 1, 0).endVertex();
            buffer.pos(x1, y1, z1).tex(0, 0).color(r, g, b, a).normal(0, 1, 0).endVertex();
        }
        tessellator.draw();
    }

    public static Vec3d getHandPosition(EnumHand hand) {
        EntityPlayer player = Minecraft.getMinecraft().player;
        Vec3d look = player.getLook(1);
        Vec3d origin = player.getPositionEyes(1);
        boolean right = hand == EnumHand.MAIN_HAND && player.getPrimaryHand() == EnumHandSide.RIGHT;
        Vec3d sideOffset = look.crossProduct(new Vec3d(0, right ? 1 : -1, 0));
        return origin.add(look.scale(0.6)).add(sideOffset.scale(0.5)).add(sideOffset.crossProduct(look).scale(0.2));
    }

    public static Vec3d getHandPosition(EntityPlayer player, EnumHand hand) {
        Vec3d look = DirectionUtils.getDirectionVecXZDegrees(player.rotationYaw);
        Vec3d origin = player.getPositionEyes(1);
        return origin.add(look.scale(0.05)).addVector(0, -0.75, 0).add(DirectionUtils.getDirectionVecXZ(player.rotationYaw
                + (hand == EnumHand.MAIN_HAND && player.getPrimaryHand() == EnumHandSide.RIGHT ? -90 : 90)).scale(0.01f));
    }

}
