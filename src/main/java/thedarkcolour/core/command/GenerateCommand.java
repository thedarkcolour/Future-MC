package thedarkcolour.core.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.SyntaxErrorException;
import net.minecraft.server.MinecraftServer;
import thedarkcolour.core.block.Generator;

public class GenerateCommand extends CommandBase {
    @Override
    public String getName() {
        return "generate";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 0) {
            throw new SyntaxErrorException("Pick something to generate!");
        }

        String arg = args[0];

        switch (arg) {
            case "blockstate":
                Generator.generateBlockStates();
                break;
            case "registry":
                Generator.generateRegistries();
                break;
            case "mappedRegistry":
                Generator.generateMappedRegistries();
                break;
        }
    }
}