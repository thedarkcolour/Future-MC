package thedarkcolour.futuremc.command

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer

object LocateBiomeCommand : CommandBase() {
    override fun getName(): String {
        return "locatebiome"
    }

    override fun getRequiredPermissionLevel(): Int {
        return 2
    }

    override fun getUsage(sender: ICommandSender): String {
        return "futuremc.commands.locatebiome.usage"
    }

    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        TODO("not implemented")
    }

    // do the autocomplete thing
}