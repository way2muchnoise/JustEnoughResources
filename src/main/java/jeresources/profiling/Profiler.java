package jeresources.profiling;

import jeresources.json.ProfilingAdapter;
import jeresources.utils.ModList;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.Loader;
import thaumcraft.common.Thaumcraft;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Profiler implements Runnable
{
    private final ExecutorService executor;
    private final ConcurrentMap<Block, Integer[]> map;
    private final ProfilingTimer timer;
    private final ICommandSender sender;
    private final int width;

    private Profiler(ICommandSender sender, int chunks)
    {
        this.sender = sender;
        this.executor = Executors.newFixedThreadPool(5);
        map = new ConcurrentHashMap<>();
        width = (int)Math.round(Math.sqrt(chunks));
        this.timer = new ProfilingTimer(sender, width);
    }

    @Override
    public void run()
    {
        WorldServer world = (WorldServer) sender.getEntityWorld();
        int offset = width / 2;
        if (Loader.isModLoaded(ModList.Names.THAUMCRAFT))
            Thaumcraft.proxy.getAuraThread().stop();
        for (int i = 0; i < width; i++)
            for (int j = 0; j < width; j++)
                world.addScheduledTask(new ChunkGetter(world, sender.getPosition(), i - offset, j - offset));
        while (!timer.isCompleted()) {}
        this.executor.shutdown();
        while (!this.executor.isTerminated()) {}
        if (Loader.isModLoaded(ModList.Names.THAUMCRAFT))
            Thaumcraft.proxy.getAuraThread().run();
        Map<Block, Float[]> distribs = new HashMap<>();
        for (Map.Entry<Block, Integer[]> entry : this.map.entrySet())
        {
            Float[] array = new Float[ChunkProfiler.CHUNK_HEIGHT];
            for (int i = 0; i < ChunkProfiler.CHUNK_HEIGHT; i++)
                array[i] = entry.getValue()[i] * 1.0F / timer.getBlocksPerLayer();
            distribs.put(entry.getKey(), array);
        }
        ProfilingAdapter.write(distribs);
        this.timer.complete();
    }

    private void addChunkProfiler(Chunk chunk)
    {
        executor.execute(new ChunkProfiler(chunk, map, timer));
    }

    public static void newChunkProfiler(Chunk chunk)
    {
        if (currentProfiler != null)
            currentProfiler.addChunkProfiler(chunk);
    }

    private static Profiler currentProfiler;

    public static boolean init(ICommandSender sender, int chunks)
    {
        if (currentProfiler != null && !currentProfiler.timer.isCompleted()) return false;
        currentProfiler = new Profiler(sender, chunks);
        new Thread(currentProfiler).start();
        return true;
    }
}
