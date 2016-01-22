package jeresources.profiling;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.DimensionManager;

import java.util.HashMap;
import java.util.Map;

public class ProfilingTimer
{
    private long[] start;
    private final ICommandSender sender;
    private int[] chunkCounter, threadCounter;
    private int totalChunks;
    private boolean allWorlds;
    private boolean[] completed;
    private Map<String, Integer> dims;

    public ProfilingTimer(ICommandSender sender, int chunkCount, boolean allWorlds)
    {
        this.sender = sender;
        this.start = new long[allWorlds ? 3 : 1];
        this.chunkCounter = new int[allWorlds ? 3 : 1];
        this.threadCounter = new int[allWorlds ? 3 : 1];
        this.completed = new boolean[allWorlds ? 3 : 1];
        this.totalChunks = chunkCount;
        this.allWorlds = allWorlds;
        this.dims = new HashMap<>();
    }

    public void startChunk(int dim)
    {
        dim += allWorlds ? 1 : 0;
        if (this.start[dim] == 0)
            this.start[dim] = System.currentTimeMillis();
        this.threadCounter[dim]++;
    }

    public void endChunk(int dim)
    {
        dim += allWorlds ? 1 : 0;
        this.threadCounter[dim]--;
        if (++this.chunkCounter[dim] % 100 == 0)
            sendSpeed(dim);
        if (this.totalChunks == this.chunkCounter[dim])
            this.completed[dim] = true;
    }

    public void complete()
    {
        for (int i = 0; i < this.completed.length; i++)
        {
            this.completed[i] = true;
            send("[" + DimensionManager.getProvider(i - (allWorlds ? 1 : 0)).getDimensionName() + "] Completed profiling of " +
                    (getBlocksPerLayer(i) * ChunkProfiler.CHUNK_HEIGHT) + " blocks in " +
                    (System.currentTimeMillis() - this.start[i]) + " ms saved to blocks.json");
        }
    }

    public synchronized boolean isCompleted()
    {
        boolean completed = true;
        for (boolean worldCompleted : this.completed)
            completed &= worldCompleted;
        return completed;
    }

    private void send(String s)
    {
        this.sender.addChatMessage(new ChatComponentText(s));
    }

    private void sendSpeed(int dim)
    {
        float time = (System.currentTimeMillis() - this.start[dim]) * 1.0F / this.chunkCounter[dim];
        String message = "[" + DimensionManager.getProvider(dim - (allWorlds ? 1 : 0)).getDimensionName() + "] Scanned " +
                this.chunkCounter[dim] + " chunks at " + String.format("%3.2f", time) + " ms/chunk";
        send(message);
    }

    public long getBlocksPerLayer(int dim)
    {
        return this.chunkCounter[dim] * ChunkProfiler.CHUNK_SIZE * ChunkProfiler.CHUNK_SIZE;
    }

    public long getBlocksPerLayer(String dim)
    {
        return getBlocksPerLayer(this.dims.get(dim) + (allWorlds ? 1 : 0));
    }

    public ProfilingTimer addDim(String dimName, int dimId)
    {
        this.dims.put(dimName, dimId);
        return this;
    }
}
