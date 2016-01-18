package jeresources.profiling;

import jeresources.utils.LogHelper;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

public class ProfilingTimer
{
    private long start;
    private final ICommandSender sender;
    private int chunkCounter, threadCounter, totalChunks;
    private boolean completed;

    public ProfilingTimer(ICommandSender sender, int chunkCount)
    {
        this.sender = sender;
        this.chunkCounter = 0;
        this.threadCounter = 0;
        this.completed = false;
        this.totalChunks = chunkCount;
    }

    public void startChunk()
    {
        if (this.start == 0)
            this.start = System.currentTimeMillis();
        this.threadCounter++;
    }

    public void endChunk()
    {
        this.threadCounter--;
        if (++this.chunkCounter % 100 == 0)
            sendSpeed(true);
        if (this.totalChunks == this.chunkCounter)
            this.completed = true;
    }

    public void complete()
    {
        this.completed = true;
        sendSpeed(false);
        send("Completed profiling of " + (getBlocksPerLayer() * ChunkProfiler.CHUNK_HEIGHT) + " blocks in " + (System.currentTimeMillis() - this.start) + " ms saved to blocks.json");
    }

    public synchronized boolean isCompleted()
    {
        return this.completed;
    }

    private void send(String s)
    {
        this.sender.addChatMessage(new ChatComponentText(s));
    }

    private void sendSpeed(boolean log)
    {
        float time = (System.currentTimeMillis() - this.start) * 1.0F / this.chunkCounter;
        String message = "Scanned " + this.chunkCounter + " chunks at " + String.format("%3.2f", time) + " ms/chunk";
        if (!log)
            send(message);
        else
            LogHelper.info(message);
    }

    public long getBlocksPerLayer()
    {
        return this.chunkCounter * ChunkProfiler.CHUNK_SIZE * ChunkProfiler.CHUNK_SIZE;
    }
}
