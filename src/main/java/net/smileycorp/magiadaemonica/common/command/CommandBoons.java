package net.smileycorp.magiadaemonica.common.command;

import com.google.common.collect.Lists;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.smileycorp.magiadaemonica.common.Constants;
import net.smileycorp.magiadaemonica.common.capabilities.Boons;
import net.smileycorp.magiadaemonica.common.capabilities.DaemonicaCapabilities;
import net.smileycorp.magiadaemonica.common.demons.contracts.BoonRegistry;

import javax.annotation.Nullable;
import java.util.List;

public class CommandBoons extends CommandBase {

    private final List<String> commands = Lists.newArrayList("add", "clear", "get", "remove");

    @Override
    public String getName() {
        return "boons";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "/boons <player> <add:clear:get:remove>";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        if (args.length == 2) return getListOfStringsMatchingLastWord(args, commands);
        if (args.length >= 2 && "add".equals(args[1]) || "remove".equals(args[1])) return getListOfStringsMatchingLastWord(args, BoonRegistry.getBoonNames());
        return Lists.newArrayList();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        for (EntityPlayerMP player : getPlayers(server, sender, args[0])) server.addScheduledTask(() -> {
            try {
                String command = args[1];
                if (!commands.contains(command)) {
                    notifyCommandListener(sender, this, "command" + Constants.MODID + ".boons.failure.subcommand", command);
                    return;
                }
                if (!player.hasCapability(DaemonicaCapabilities.BOONS, null)) throw new CommandException("");
                if (command.equals("get")) {
                    String boons = player.getCapability(DaemonicaCapabilities.BOONS, null).toString();
                    if (boons.isEmpty()) notifyCommandListener(sender, this, "command." + Constants.MODID + ".boons.get.failure", player.getDisplayName());
                    else notifyCommandListener(sender, this, "command." + Constants.MODID + ".boons.get.success", player.getDisplayName(), boons);
                    return;
                }
                if (command.equals("clear")) {
                    notifyCommandListener(sender, this, "command." + Constants.MODID + ".boons.clear.success", player.getDisplayName());
                    Boons.clear(player);
                    return;
                }
                if (args.length < 3) {
                    notifyCommandListener(sender, this, "command" + Constants.MODID + ".boons.failure.args", command);
                    return;
                }
                ResourceLocation boon = new ResourceLocation(args[2]);
                if (command.equals("add")) {
                    int newLevel = Boons.add(player, boon);
                    if (newLevel == 0) notifyCommandListener(sender, this, "command" + Constants.MODID + ".boons.add.failure", player.getDisplayName(), boon);
                    else notifyCommandListener(sender, this, "command." + Constants.MODID + ".boons.add.success", player.getDisplayName(), boon, newLevel);
                    return;
                }
                if (command.equals("remove")) {
                    if (Boons.remove(player, boon)) notifyCommandListener(sender, this, "command" + Constants.MODID + ".boons.remove.success", player.getDisplayName(), boon);
                    else notifyCommandListener(sender, this, "command." + Constants.MODID + ".boons.remove.failure", player.getDisplayName(), boon);
                }
            } catch (Exception e) {}
        });
    }

}
