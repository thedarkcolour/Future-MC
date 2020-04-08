package thedarkcolour.core.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class HealCommand extends CommandBase {
    @Override
    public String getName() {
        return "heal";
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
        if (args.length == 0) {
            EntityPlayer entityplayer = getCommandSenderAsPlayer(sender);
            entityplayer.heal(1000);
        } else {
            Entity entity = getEntity(server, sender, args[0]);
            if (entity instanceof EntityLivingBase) {
                ((EntityLivingBase) entity).heal(1000);
            }
        }
    }
}