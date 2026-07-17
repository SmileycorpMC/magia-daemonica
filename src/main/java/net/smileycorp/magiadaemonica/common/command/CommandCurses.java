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
import net.smileycorp.magiadaemonica.common.capabilities.Curses;
import net.smileycorp.magiadaemonica.common.capabilities.DaemonicaCapabilities;
import net.smileycorp.magiadaemonica.common.demons.contracts.CursesRegistry;
import net.smileycorp.magiadaemonica.common.network.ChooseCurseBoonMessage;

import javax.annotation.Nullable;
import java.util.List;

public class CommandCurses extends CommandBase {

    private final List<String> commands = Lists.newArrayList("add", "choose", "clear", "get", "remove");

    @Override
    public String getName() {
        return "curses";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "/curses <player> <add:choose:clear:get:remove>";
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos) {
        if (args.length == 1) return getListOfStringsMatchingLastWord(args, server.getOnlinePlayerNames());
        if (args.length == 2) return getListOfStringsMatchingLastWord(args, commands);
        if (args.length >= 2 && "add".equals(args[1]) || "remove".equals(args[1])) return getListOfStringsMatchingLastWord(args, CursesRegistry.getCurseNames());
        return Lists.newArrayList();
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        for (EntityPlayerMP player : getPlayers(server, sender, args[0])) server.addScheduledTask(() -> {
            try {
                String command = args[1];
                if (!commands.contains(command)) {
                    notifyCommandListener(sender, this, "command" + Constants.MODID + ".curses.failure.subcommand", command);
                    return;
                }
                if (!player.hasCapability(DaemonicaCapabilities.CURSES, null)) throw new CommandException("");
                if (command.equals("get")) {
                    String curses = player.getCapability(DaemonicaCapabilities.CURSES, null).toString();
                    if (curses.isEmpty()) notifyCommandListener(sender, this, "command." + Constants.MODID + ".curses.get.failure", player.getDisplayName());
                    else notifyCommandListener(sender, this, "command." + Constants.MODID + ".curses.get.success", player.getDisplayName(), curses);
                    return;
                }
                if (command.equals("clear")) {
                    notifyCommandListener(sender, this, "command." + Constants.MODID + ".curses.clear.success", player.getDisplayName());
                    Curses.clear(player);
                    return;
                }
                if (command.equals("choose")) {
                    int amount = args.length < 3 ? 3 : parseInt(args[2]);
                    ChooseCurseBoonMessage.send(player, true, CursesRegistry.getRandomCurses(player, amount));
                    return;
                }
                if (args.length < 3) {
                    notifyCommandListener(sender, this, "command" + Constants.MODID + ".curses.failure.args", command);
                    return;
                }
                ResourceLocation curse = new ResourceLocation(args[2]);
                if (command.equals("add")) {
                    int newLevel = Curses.add(player, curse);
                    if (newLevel == 0) notifyCommandListener(sender, this, "command" + Constants.MODID + ".curses.add.failure", player.getDisplayName(), curse);
                    else notifyCommandListener(sender, this, "command." + Constants.MODID + ".curses.add.success", player.getDisplayName(), curse, newLevel);
                    return;
                }
                if (command.equals("remove")) {
                    if (Curses.remove(player, curse)) notifyCommandListener(sender, this, "command" + Constants.MODID + ".curses.remove.success", player.getDisplayName(), curse);
                    else notifyCommandListener(sender, this, "command." + Constants.MODID + ".curses.remove.failure", player.getDisplayName(), curse);
                }
            } catch (Exception e) {}
        });
    }

}
