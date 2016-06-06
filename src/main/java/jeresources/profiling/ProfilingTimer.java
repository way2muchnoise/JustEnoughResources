package jeresources.profiling;

import jeresources.config.ConfigHandler;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.DimensionManager;

import java.util.HashMap;
import java.util.Map;

public class ProfilingTimer
{
    private final ICommandSender sender;
    private int totalChunks;
    private final Map<Integer, DimensionCounters> dimensionsMap = new HashMap<>();

    private static class DimensionCounters
    {
        public final long start = System.currentTimeMillis();
        public int chunkCounter;
        public int threadCounter;
        public boolean completed;
    }

    public ProfilingTimer(ICommandSender sender, int chunkCount)
    {
        this.sender = sender;
        this.totalChunks = chunkCount;
    }

    public void startChunk(int dim)
    {
        DimensionCounters counters = this.dimensionsMap.get(dim);
        if (counters == null)
        {
            counters = new DimensionCounters();
            this.dimensionsMap.put(dim, counters);
            send("[" + DimensionManager.getProvider(dim).getDimensionType().getName() + "] Started profiling");
        }
        counters.threadCounter++;
    }

    public void endChunk(int dim)
    {
        DimensionCounters counters = dimensionsMap.get(dim);
        counters.threadCounter--;
        if (++counters.chunkCounter % 100 == 0)
            sendSpeed(dim);
        if (this.totalChunks == counters.chunkCounter)
            counters.completed = true;
    }

    public void complete()
    {
        for (int dim : this.dimensionsMap.keySet())
        {
            DimensionCounters counters = dimensionsMap.get(dim);
            counters.completed = true;
            send("[" + DimensionManager.getProvider(dim).getDimensionType().getName() + "] Completed profiling of " +
                    (getBlocksPerLayer(dim) * ChunkProfiler.CHUNK_HEIGHT) + " blocks in " +
                    (System.currentTimeMillis() - counters.start) + " ms saved to " + ConfigHandler.getWorldGenFile());
        }
    }

    public synchronized boolean isCompleted()
    {
        for (DimensionCounters counters : dimensionsMap.values())
            if (!counters.completed)
                return false;

        return true;
    }

    private void send(String s)
    {
        this.sender.addChatMessage(new TextComponentTranslation(s));
    }

    private void sendSpeed(int dim)
    {
        DimensionCounters counters = dimensionsMap.get(dim);
        float time = (System.currentTimeMillis() - counters.start) * 1.0F / counters.chunkCounter;
        String message = "[" + DimensionManager.getProvider(dim).getDimensionType().getName() + "] Scanned " +
                counters.chunkCounter + " chunks at " + String.format("%3.2f", time) + " ms/chunk";
        send(message);
    }

    public long getBlocksPerLayer(int dim)
    {
        DimensionCounters counters = dimensionsMap.get(dim);
        return counters.chunkCounter * ChunkProfiler.CHUNK_SIZE * ChunkProfiler.CHUNK_SIZE;
    }
}
