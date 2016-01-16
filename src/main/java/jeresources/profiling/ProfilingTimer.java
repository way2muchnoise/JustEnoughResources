package jeresources.profiling;

import jeresources.utils.LogHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class ProfilingTimer
{
    private final long start;
    private final long blocks;
    private final ICommandSender sender;
    private int chunkCounter, threadCounter;

    public ProfilingTimer(ICommandSender sender, long blocks)
    {
        this.start = System.currentTimeMillis();
        this.sender = sender;
        this.blocks = blocks;
        this.chunkCounter = 0;
        this.threadCounter = 0;
    }

    public void startChunk()
    {
        threadCounter++;
    }

    public void endChunk()
    {
        threadCounter--;
        if (++chunkCounter % 100 == 0)
            sendSpeed(true);
    }

    public void completed()
    {
        sendSpeed(false);
        send("Completed profiling of " + (blocks * ChunkProfiler.CHUNK_HEIGHT) + " blocks in " + (System.currentTimeMillis() - start) + " ms saved to blocks.json");
    }

    private void send(String s)
    {
        sender.addChatMessage(new ChatComponentText(s));
    }

    private void sendSpeed(boolean log)
    {
        float time = (System.currentTimeMillis() - start) * 1.0F / chunkCounter;
        String message = "Scanned " + chunkCounter + " chunks at " + String.format("%3.2f", time) + " ms/chunk";
        if (!log)
            send(message);
        else
            LogHelper.info(message);
    }
}
