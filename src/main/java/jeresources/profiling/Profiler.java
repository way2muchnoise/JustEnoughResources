package jeresources.profiling;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import net.minecraft.command.ICommandSender;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

import net.minecraftforge.fml.common.Loader;

import jeresources.compatibility.thaumcraft.ThaumcraftCompat;
import jeresources.json.ProfilingAdapter;
import jeresources.utils.ModList;

public class Profiler implements Runnable
{
    private final ExecutorService executor;
    private final ConcurrentMap<String, Integer[]> map;
    private final ProfilingTimer timer;
    private final ICommandSender sender;
    private final int chunkCount;

    private Profiler(ICommandSender sender, int chunkCount)
    {
        this.sender = sender;
        final int processors = Runtime.getRuntime().availableProcessors();
        this.executor = Executors.newFixedThreadPool(processors - 1);
        this.map = new ConcurrentHashMap<>();
        this.chunkCount = chunkCount;
        this.timer = new ProfilingTimer(sender, chunkCount);
    }

    @Override
    public void run()
    {
        if (Loader.isModLoaded(ModList.Names.THAUMCRAFT))
            ThaumcraftCompat.stopAuraThread();

        WorldServer world = (WorldServer) this.sender.getEntityWorld();
        DummyWorld dummyWorld = new DummyWorld(world);
        dummyWorld.init();
        final int chunkGetterCount = (int) Math.ceil(chunkCount / (float) ChunkGetter.CHUNKS_PER_RUN);
        for (int i = 0; i < chunkGetterCount; i++)
            dummyWorld.addScheduledTask(new ChunkGetter(dummyWorld, this));

        dummyWorld.addScheduledTask(new Runnable() {
            @Override
            public void run() {
                executor.shutdown();
            }
        });

        while (true) {
            try {
                if (executor.awaitTermination(10, TimeUnit.SECONDS)) {
                    break;
                }
            } catch (InterruptedException ex) {
                // continue waiting
            }
        }

        writeData();

        this.timer.complete();
    }

    public void addChunkProfiler(DummyWorld dummyWorld, List<Chunk> chunks)
    {
        this.executor.execute(new ChunkProfiler(dummyWorld, chunks, this.map, this.timer));
    }

    private void writeData()
    {
        Map<String, Float[]> distribs = new HashMap<>();
        for (Map.Entry<String, Integer[]> entry : this.map.entrySet())
        {
            Float[] array = new Float[ChunkProfiler.CHUNK_HEIGHT];
            for (int i = 0; i < ChunkProfiler.CHUNK_HEIGHT; i++)
                array[i] = entry.getValue()[i] * 1.0F / this.timer.getBlocksPerLayer();
            distribs.put(entry.getKey(), array);
        }
        ProfilingAdapter.write(distribs);
    }

    private static Profiler currentProfiler;

    public static boolean init(ICommandSender sender, int chunks)
    {
        if (currentProfiler != null && !currentProfiler.timer.isCompleted()) return false;
        currentProfiler = new Profiler(sender, chunks);
        new Thread(currentProfiler).start();
        return true;
    }

    public static boolean stop() {
        if (currentProfiler == null || currentProfiler.timer.isCompleted()) return false;
        currentProfiler.executor.shutdownNow();
        currentProfiler.writeData();
        return true;
    }
}
