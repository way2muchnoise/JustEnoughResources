package jeresources.profiling;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentText;

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
        return "profile [chunks]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length == 1)
        {
            int chunks;
            try
            {
                chunks = Integer.parseInt(args[0]);
            } catch(NumberFormatException e)
            {
                throw new WrongUsageException("[chunks] has to be a positive integer");
            }
            if (chunks <= 0) throw new WrongUsageException("[chunks] has to be a positive integer");
            sender.addChatMessage(new ChatComponentText(Profiler.profileChunks(sender.getEntityWorld(), sender.getPosition(), chunks)));
        } else
        {
            throw new WrongUsageException(getCommandUsage(sender));
        }
    }
}
