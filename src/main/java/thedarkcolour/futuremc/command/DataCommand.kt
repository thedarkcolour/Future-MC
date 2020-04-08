package thedarkcolour.futuremc.command

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer

object DataCommand : CommandBase() {
    override fun getRequiredPermissionLevel() = 2

    override fun getName() = "data"

    override fun getUsage(sender: ICommandSender) = "commands.futuremc.data.usage"

    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        TODO("not implemented")
    }
}