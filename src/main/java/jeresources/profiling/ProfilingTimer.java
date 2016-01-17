package jeresources.profiling;

import jeresources.utils.LogHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class ProfilingTimer
{
    private final long start;
    private final ICommandSender sender;
    private int chunkCounter, threadCounter, totalChunks;
    private boolean completed;

    public ProfilingTimer(ICommandSender sender, int width)
    {
        this.start = System.currentTimeMillis();
        this.sender = sender;
        this.chunkCounter = 0;
        this.threadCounter = 0;
        this.completed = false;
        this.totalChunks = width * width;
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
        if (totalChunks == chunkCounter)
            completed = true;
    }

    public void complete()
    {
        completed = true;
        sendSpeed(false);
        send("Completed profiling of " + (getBlocksPerLayer() * ChunkProfiler.CHUNK_HEIGHT) + " blocks in " + (System.currentTimeMillis() - start) + " ms saved to blocks.json");
    }

    public synchronized boolean isCompleted()
    {
        return completed;
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

    public long getBlocksPerLayer()
    {
        return chunkCounter * ChunkProfiler.CHUNK_SIZE * ChunkProfiler.CHUNK_SIZE;
    }
}
