package jeresources.profiling;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.server.MinecraftServer;

public class ProfileCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
            Commands.literal("jer_profile")
            .requires(source -> source.hasPermissionLevel(4) && Minecraft.getInstance().isSingleplayer())
            .then(Commands.argument("chunk count", IntegerArgumentType.integer(1))
                .executes((context -> Profiler.stop() ? Command.SINGLE_SUCCESS : 0)))
            .then(Commands.literal("stop")
                .executes(context -> Profiler.stop() ? Command.SINGLE_SUCCESS : 0))
        );
    }

    /*
    @Override
    public String getUsage(ICommandSender sender) {
        return "jer_profile [chunks|stop] [all]";
    }
    */
}
