package net.smileycorp.magiadaemonica.common.invocations;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface Invocation {

    InvocationResult apply(String invocation, EntityPlayer player);

    interface ClientInvocation {

        void writeToBuf(ByteBuf buf, Object... args);

        Object[] readFromBuf(ByteBuf buf);

        @SideOnly(Side.CLIENT)
        void packetReceived(EntityPlayer player, Object... args);

    }

    class InvocationResult {

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
