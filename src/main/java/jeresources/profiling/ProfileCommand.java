package jeresources.profiling;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

public class ProfileCommand extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return "profile";
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "profile [chunks|stop] [all]";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length > 0 && args.length < 3)
        {
            if (!Minecraft.getMinecraft().isSingleplayer())
                throw new WrongUsageException("can't use command in multiplayer");

            if ("stop".equals(args[0]))
            {
                if (!Profiler.stop())
                    throw new WrongUsageException("Not profiling, run \"/profile [chunks]\" to start");
                else return;
            }

            int chunks;
            try
            {
                chunks = Integer.parseInt(args[0]);
            } catch (NumberFormatException e)
            {
                throw new WrongUsageException("[chunks] has to be a positive integer");
            }
            if (chunks <= 0) throw new WrongUsageException("[chunks] has to be a positive integer");
            if (!Profiler.init(sender, chunks, args.length == 2 && args[1].equals("all")))
                throw new WrongUsageException("Already profiling run \"/profile stop\" to stop");
        } else
        {
            throw new WrongUsageException(getCommandUsage(sender));
        }
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 4;
    }
}
