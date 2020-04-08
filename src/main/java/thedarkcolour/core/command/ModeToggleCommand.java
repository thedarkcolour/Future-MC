package thedarkcolour.core.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.GameType;

public class ModeToggleCommand extends CommandBase {
    @Override
    public String getName() {
        return "c";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        EntityPlayerMP player = getCommandSenderAsPlayer(sender);
        if (player.interactionManager.getGameType() != GameType.SURVIVAL) {
            player.setGameType(GameType.SURVIVAL);
        } else {
            player.setGameType(GameType.CREATIVE);
        }
    }
}