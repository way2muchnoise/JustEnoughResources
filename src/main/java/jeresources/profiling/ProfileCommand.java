package jeresources.profiling;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;

public class ProfileCommand {

    public static void register(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(
            Commands.literal("jer_profile")
            .requires(source -> source.hasPermission(4) && Minecraft.getInstance().hasSingleplayerServer())
            .then(Commands.argument("chunk count", IntegerArgumentType.integer(1))
                .executes((context -> Profiler.stop() ? Command.SINGLE_SUCCESS : 0)))
            .then(Commands.literal("stop")
                .executes(context -> Profiler.stop() ? Command.SINGLE_SUCCESS : 0))
        );
    }
}
