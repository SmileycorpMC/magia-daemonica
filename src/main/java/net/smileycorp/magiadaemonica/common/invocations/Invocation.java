package net.smileycorp.magiadaemonica.common.invocations;

import com.google.common.collect.Lists;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.smileycorp.magiadaemonica.common.invocations.components.MagiaComponent;

import java.util.List;

public abstract class Invocation {

    private final List<MagiaComponent> components = Lists.newArrayList();
    private ResourceLocation registryName;

    public void setRegistryName(ResourceLocation registryName) {
        this.registryName = registryName;
    }

    public ResourceLocation getRegistryName() {
        return registryName;
    }

    public void addComponent(MagiaComponent component) {
        components.add(component);
    }

    public boolean canApply(InvocationContext ctx) {
        for (MagiaComponent component : components) {
            System.out.println(component + ", " + component.canApply(ctx));
            if (!(component.canApply(ctx))) return false;
        }
        return true;
    }

    public abstract InvocationResult apply(InvocationContext ctx);

    public void consumeComponents(InvocationContext ctx) {
        components.forEach(component -> component.consumeComponent(ctx));
    }

    public interface ClientInvocation {

        void writeToBuf(ByteBuf buf, Object... args);

        Object[] readFromBuf(ByteBuf buf);

        @SideOnly(Side.CLIENT)
        void packetReceived(EntityPlayer player, Object... args);

    }

    public static class InvocationResult {

        private static final InvocationResult SUCCESS = new InvocationResult();

        private final Object[] args;

        private InvocationResult(Object... args) {
            this.args = args;
        }

        public static InvocationResult success() {
            return SUCCESS;
        }

        public static InvocationResult withArgs(Object... args) {
            return new InvocationResult(args);
        }

        public static InvocationResult optionalSuccess(boolean success) {
            return success ? SUCCESS : null;
        }

        public Object[] getArgs() {
            return args;
        }

    }

}
