package thedarkcolour.futuremc.command

import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer

object FMCCommand : CommandBase() {
    override fun getRequiredPermissionLevel() = 2

    override fun getName() = "futuremc"

    override fun getAliases() = arrayListOf("fmc")

    override fun getUsage(sender: ICommandSender) = "Command success"

    override fun execute(server: MinecraftServer, sender: ICommandSender, args: Array<String>) {
        TODO("not implemented")
    }
}