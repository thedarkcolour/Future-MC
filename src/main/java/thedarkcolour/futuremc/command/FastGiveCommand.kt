package thedarkcolour.futuremc.command

import net.minecraft.command.CommandBase
import net.minecraft.command.CommandGive
import net.minecraft.command.ICommandSender
import net.minecraft.server.MinecraftServer
import net.minecraft.util.math.BlockPos


object FastGiveCommand : CommandGive() {
    val registryKeys = hashMapOf<String, String>()

    override fun getName(): String {
        @Suppress("SpellCheckingInspection")
        return "fgive"
    }

    override fun getTabCompletions(
        server: MinecraftServer, sender: ICommandSender, args: Array<String>, targetPos: BlockPos?
    ): List<String> {
        return if (args.size == 1) {
            CommandBase.getListOfStringsMatchingLastWord(args, *server.onlinePlayerNames)
        } else {
            if (args.size == 2) {
                val typed = args[args.size - 1]
                val list = arrayListOf<String>()

                for (entry in registryKeys.keys) {
                    if (entry.startsWith(typed)) {
                        list.add(registryKeys[entry]!!)
                    }
                }

                list
            } else {
                emptyList()
            }
        }
    }
}