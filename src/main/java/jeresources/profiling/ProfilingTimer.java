package jeresources.profiling;

import jeresources.json.WorldGenAdapter;
import jeresources.util.DimensionHelper;
import net.minecraft.Util;
import net.minecraft.commands.CommandSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

import java.util.HashMap;
import java.util.Map;

public class ProfilingTimer {
    private final CommandSource sender;
    private int totalChunks;
    private final Map<ResourceKey<Level>, DimensionCounters> dimensionsMap = new HashMap<>();

    private static class DimensionCounters {
        public final long start = System.currentTimeMillis();
        public int chunkCounter;
        public int threadCounter;
        public boolean completed;
    }

    public ProfilingTimer(CommandSource sender, int chunkCount) {
        this.sender = sender;
        this.totalChunks = chunkCount;
    }

    public void startChunk(ResourceKey<Level> worldRegistryKey) {
        DimensionCounters counters = this.dimensionsMap.get(worldRegistryKey);
        if (counters == null) {
            counters = new DimensionCounters();
            this.dimensionsMap.put(worldRegistryKey, counters);
            send("[" + DimensionHelper.getDimensionName(worldRegistryKey) + "] Started profiling");
        }
        counters.threadCounter++;
    }

    public void endChunk(ResourceKey<Level> worldRegistryKey) {
        DimensionCounters counters = dimensionsMap.get(worldRegistryKey);
        counters.threadCounter--;
        if (++counters.chunkCounter % 100 == 0)
            sendSpeed(worldRegistryKey);
        if (this.totalChunks == counters.chunkCounter)
            counters.completed = true;
    }

    public void complete() {
        for (ResourceKey<Level> worldRegistryKey : this.dimensionsMap.keySet()) {
            DimensionCounters counters = dimensionsMap.get(worldRegistryKey);
            counters.completed = true;
            send("[" + DimensionHelper.getDimensionName(worldRegistryKey) + "] Completed profiling of " +
                (getBlocksPerLayer(worldRegistryKey) * ChunkProfiler.CHUNK_HEIGHT) + " blocks in " +
                (System.currentTimeMillis() - counters.start) + " ms saved to " + WorldGenAdapter.getWorldGenFile());
        }
    }

    public synchronized boolean isCompleted() {
        for (DimensionCounters counters : dimensionsMap.values())
            if (!counters.completed)
                return false;

        return true;
    }

    private void send(String s) {
        this.sender.sendSystemMessage(Component.translatable(s));
    }

    private void sendSpeed(ResourceKey<Level> worldRegistryKey) {
        DimensionCounters counters = dimensionsMap.get(worldRegistryKey);
        float time = (System.currentTimeMillis() - counters.start) * 1.0F / counters.chunkCounter;
        String message = "[" + DimensionHelper.getDimensionName(worldRegistryKey) + "] Scanned " +
            counters.chunkCounter + " chunks at " + String.format("%3.2f", time) + " ms/chunk";
        send(message);
    }

    public long getBlocksPerLayer(ResourceKey<Level> worldRegistryKey) {
        DimensionCounters counters = dimensionsMap.get(worldRegistryKey);
        return counters.chunkCounter * ChunkProfiler.CHUNK_SIZE * ChunkProfiler.CHUNK_SIZE;
    }
}
