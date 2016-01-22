package jeresources.profiling;

import jeresources.json.ProfilingAdapter;

import net.minecraft.command.ICommandSender;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Profiler implements Runnable
{
    private final ExecutorService executor;
    private final ConcurrentMap<String, Integer[]> distributionMap;
    private final ConcurrentMap<String, Boolean> silkTouchMap;
    private final ConcurrentMap<String, Map<String, Float>> dropsMap;
    private final ProfilingTimer timer;
    private final ICommandSender sender;
    private final int chunkCount;

    private Profiler(ICommandSender sender, int chunkCount)
    {
        this.sender = sender;
        final int processors = Runtime.getRuntime().availableProcessors();
        this.executor = Executors.newFixedThreadPool(processors - 1);
        this.distributionMap = new ConcurrentHashMap<>();
        this.silkTouchMap = new ConcurrentHashMap<>();
        this.dropsMap = new ConcurrentHashMap<>();
        this.chunkCount = chunkCount;
        this.timer = new ProfilingTimer(sender, chunkCount);
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    @Override
    public void run()
    {
        WorldServer world = (WorldServer) this.sender.getEntityWorld();
        int chunkGetterRunCount = (int) Math.ceil(chunkCount / (float) ChunkGetter.CHUNKS_PER_RUN);

        ChunkGetter chunkGetter = new ChunkGetter(chunkGetterRunCount, world, this);
        world.addScheduledTask(chunkGetter);

        while (true)
        {
            try
            {
                if (executor.awaitTermination(10, TimeUnit.SECONDS))
                {
                    break;
                }
            } catch (InterruptedException ex)
            {
                // continue waiting
            }
        }

        writeData();

        this.timer.complete();
    }

    public void addChunkProfiler(DummyWorld dummyWorld, List<Chunk> chunks)
    {
        try
        {
            this.executor.execute(new ChunkProfiler(dummyWorld, chunks, this.distributionMap, this.silkTouchMap, this.dropsMap, this.timer));
        }
        catch(RejectedExecutionException ignored)
        {
            // the player has forced profiling to stop
        }
    }

    private void writeData()
    {
        Map<String, Float[]> distribs = new HashMap<>();
        for (Map.Entry<String, Integer[]> entry : this.distributionMap.entrySet())
        {
            Float[] array = new Float[ChunkProfiler.CHUNK_HEIGHT];
            for (int i = 0; i < ChunkProfiler.CHUNK_HEIGHT; i++)
                array[i] = entry.getValue()[i] * 1.0F / this.timer.getBlocksPerLayer();
            distribs.put(entry.getKey(), array);
        }

        ProfilingAdapter.write(distribs, this.silkTouchMap, this.dropsMap);
    }

    private static Profiler currentProfiler;

    public static boolean init(ICommandSender sender, int chunks)
    {
        if (currentProfiler != null && !currentProfiler.timer.isCompleted()) return false;
        currentProfiler = new Profiler(sender, chunks);
        new Thread(currentProfiler).start();
        return true;
    }

    public static boolean stop()
    {
        if (currentProfiler == null || currentProfiler.timer.isCompleted()) return false;
        currentProfiler.executor.shutdownNow();
        currentProfiler.writeData();
        return true;
    }
}
