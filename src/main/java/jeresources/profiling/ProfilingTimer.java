package jeresources.profiling;

import jeresources.json.WorldGenAdapter;
import jeresources.util.DimensionHelper;
import net.minecraft.command.ICommandSource;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class ProfilingTimer {
    private final ICommandSource sender;
    private int totalChunks;
    private final Map<RegistryKey<World>, DimensionCounters> dimensionsMap = new HashMap<>();

    private static class DimensionCounters {
        public final long start = System.currentTimeMillis();
        public int chunkCounter;
        public int threadCounter;
        public boolean completed;
    }

    public ProfilingTimer(ICommandSource sender, int chunkCount) {
        this.sender = sender;
        this.totalChunks = chunkCount;
    }

    public void startChunk(RegistryKey<World> worldRegistryKey) {
        DimensionCounters counters = this.dimensionsMap.get(worldRegistryKey);
        if (counters == null) {
            counters = new DimensionCounters();
            this.dimensionsMap.put(worldRegistryKey, counters);
            send("[" + DimensionHelper.getWorldName(worldRegistryKey) + "] Started profiling");
        }
        counters.threadCounter++;
    }

    public void endChunk(RegistryKey<World> worldRegistryKey) {
        DimensionCounters counters = dimensionsMap.get(worldRegistryKey);
        counters.threadCounter--;
        if (++counters.chunkCounter % 100 == 0)
            sendSpeed(worldRegistryKey);
        if (this.totalChunks == counters.chunkCounter)
            counters.completed = true;
    }

    public void complete() {
        for (RegistryKey<World> worldRegistryKey : this.dimensionsMap.keySet()) {
            DimensionCounters counters = dimensionsMap.get(worldRegistryKey);
            counters.completed = true;
            send("[" + DimensionHelper.getWorldName(worldRegistryKey) + "] Completed profiling of " +
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
        this.sender.sendMessage(new TranslationTextComponent(s), Util.NIL_UUID);
    }

    private void sendSpeed(RegistryKey<World> worldRegistryKey) {
        DimensionCounters counters = dimensionsMap.get(worldRegistryKey);
        float time = (System.currentTimeMillis() - counters.start) * 1.0F / counters.chunkCounter;
        String message = "[" + DimensionHelper.getWorldName(worldRegistryKey) + "] Scanned " +
            counters.chunkCounter + " chunks at " + String.format("%3.2f", time) + " ms/chunk";
        send(message);
    }

    public long getBlocksPerLayer(RegistryKey<World> worldRegistryKey) {
        DimensionCounters counters = dimensionsMap.get(worldRegistryKey);
        return counters.chunkCounter * ChunkProfiler.CHUNK_SIZE * ChunkProfiler.CHUNK_SIZE;
    }
}
