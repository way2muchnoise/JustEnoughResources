package jeresources.profiling;

import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;

public class ProfilingExecutor {
    private final ExecutorService executor;
    private final Profiler profiler;

    public ProfilingExecutor(Profiler profiler) {
        this.profiler = profiler;
        final int processors = Runtime.getRuntime().availableProcessors();
        this.executor = Executors.newFixedThreadPool(processors * 2);
    }

    public void addChunkProfiler(ServerLevel level, List<ChunkAccess> chunks) {
        final ResourceKey<Level> dimensionKey = level.dimension();
        final ProfiledDimensionData dimensionData = profiler.getAllDimensionData().get(dimensionKey);

        this.execute(new ChunkProfiler(level, dimensionKey, chunks, dimensionData, profiler.getTimer(), profiler.getBlacklist()));
    }

    public void execute(Runnable runnable) {
        try {
            this.executor.execute(runnable);
        } catch (RejectedExecutionException ignored) {
            // the player has forced profiling to stop
        }
    }

    public void shutdown() {
        this.executor.shutdown();
    }

    public void shutdownNow() {
        this.executor.shutdownNow();
    }

    public void awaitTermination() {
        while (true) {
            try {
                if (executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    break;
                }
            } catch (InterruptedException ex) {
                // continue waiting
            }
        }
    }
}
