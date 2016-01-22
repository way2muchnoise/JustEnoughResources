package jeresources.profiling;

import jeresources.json.ProfilingAdapter;

import net.minecraft.command.ICommandSender;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Profiler implements Runnable
{
    private final ExecutorService executor;
    private final ConcurrentMap<String, ConcurrentMap<String, Integer[]>> distributionMap;
    private final ConcurrentMap<String, ConcurrentMap<String, Boolean>> silkTouchMap;
    private final ConcurrentMap<String, ConcurrentMap<String, Map<String, Float>>> dropsMap;
    private final ProfilingTimer timer;
    private final ICommandSender sender;
    private final int chunkCount;
    private final boolean allWorlds;

    private Profiler(ICommandSender sender, int chunkCount, boolean allWorlds)
    {
        this.sender = sender;
        final int processors = Runtime.getRuntime().availableProcessors();
        this.executor = Executors.newFixedThreadPool(processors - 1);
        this.distributionMap = new ConcurrentHashMap<>();
        this.silkTouchMap = new ConcurrentHashMap<>();
        this.dropsMap = new ConcurrentHashMap<>();
        this.chunkCount = chunkCount;
        this.timer = new ProfilingTimer(sender, chunkCount, allWorlds);
        this.allWorlds = allWorlds;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    @Override
    public void run()
    {
        if (!allWorlds)
        {
            WorldServer world = (WorldServer) this.sender.getEntityWorld();
            int chunkGetterRunCount = (int) Math.ceil(chunkCount / (float) ChunkGetter.CHUNKS_PER_RUN);

            ChunkGetter chunkGetter = new ChunkGetter(chunkGetterRunCount, world, this);
            world.addScheduledTask(chunkGetter);
        } else
        {
            WorldServer world = DimensionManager.getWorld(-1);
            int chunkGetterRunCount = (int) Math.ceil(chunkCount / (float) ChunkGetter.CHUNKS_PER_RUN);

            ChunkGetter chunkGetter = new ChunkGetter(chunkGetterRunCount, world, this);
            world.addScheduledTask(chunkGetter);

            world = DimensionManager.getWorld(1);
            chunkGetterRunCount = (int) Math.ceil(chunkCount / (float) ChunkGetter.CHUNKS_PER_RUN);

            chunkGetter = new ChunkGetter(chunkGetterRunCount, world, this);
            world.addScheduledTask(chunkGetter);

            world = DimensionManager.getWorld(0);
            chunkGetterRunCount = (int) Math.ceil(chunkCount / (float) ChunkGetter.CHUNKS_PER_RUN);

            chunkGetter = new ChunkGetter(chunkGetterRunCount, world, this);
            world.addScheduledTask(chunkGetter);
        }



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
        addWorld(dummyWorld);
        try
        {
            this.executor.execute(new ChunkProfiler(dummyWorld, chunks,
                    this.distributionMap.get(dummyWorld.getDimName()),
                    this.silkTouchMap.get(dummyWorld.getDimName()),
                    this.dropsMap.get(dummyWorld.getDimName()), this.timer.addDim(dummyWorld.getDimName(), dummyWorld.getDimId())));
        }
        catch(RejectedExecutionException ignored)
        {
            // the player has forced profiling to stop
        }
    }

    private void addWorld(DummyWorld world)
    {
        this.distributionMap.put(world.getDimName(), new ConcurrentHashMap<String, Integer[]>());
        this.silkTouchMap.put(world.getDimName(), new ConcurrentHashMap<String, Boolean>());
        this.dropsMap.put(world.getDimName(), new ConcurrentHashMap<String, Map<String, Float>>());
    }

    private void writeData()
    {
        Map<String, Map<String, Float[]>> distribs = new HashMap<>();
        for (Map.Entry<String, ConcurrentMap<String, Integer[]>> worldEntry : this.distributionMap.entrySet())
        {
            Map<String, Float[]> worldDistribs = distribs.get(worldEntry.getKey());
            if (worldDistribs == null) worldDistribs = new HashMap<>();
            for (Map.Entry<String, Integer[]> entry : worldEntry.getValue().entrySet())
            {
                Float[] array = new Float[ChunkProfiler.CHUNK_HEIGHT];
                for (int i = 0; i < ChunkProfiler.CHUNK_HEIGHT; i++)
                    array[i] = entry.getValue()[i] * 1.0F / this.timer.getBlocksPerLayer(worldEntry.getKey());
                worldDistribs.put(entry.getKey(), array);
            }
            distribs.put(worldEntry.getKey(), worldDistribs);
        }
        ProfilingAdapter.write(distribs, this.silkTouchMap, this.dropsMap);
    }

    private static Profiler currentProfiler;

    public static boolean init(ICommandSender sender, int chunks, boolean allWorlds)
    {
        if (currentProfiler != null && !currentProfiler.timer.isCompleted()) return false;
        currentProfiler = new Profiler(sender, chunks, allWorlds);
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
