package jeresources.profiling;

import jeresources.json.ProfilingAdapter;
import net.minecraft.block.Block;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

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
    private final long blocks;
    private final ProfilingTimer timer;

    public Profiler(ICommandSender sender, int chunks)
    {
        World world = sender.getEntityWorld();
        BlockPos origin = sender.getPosition();
        this.executor = Executors.newFixedThreadPool(20);
        map = new ConcurrentHashMap<>();
        int width = (int)Math.round(Math.sqrt(chunks));
        int offset = width / 2;
        blocks = width * width * ChunkProfiler.CHUNK_SIZE * ChunkProfiler.CHUNK_SIZE; // blocks in one y level
        this.timer = new ProfilingTimer(sender, blocks);
        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < width; j++)
            {
                executor.execute(new ChunkProfiler(
                        world.getChunkFromBlockCoords(origin.add(
                                (i - offset) * ChunkProfiler.CHUNK_SIZE,
                                0 ,
                                (j - offset) * ChunkProfiler.CHUNK_SIZE)
                        ), map, timer));
            }
        }
    }

    @Override
    public void run()
    {
        this.executor.shutdown();
        while (!this.executor.isTerminated()) {}
        Map<Block, Float[]> distribs = new HashMap<>();
        for (Map.Entry<Block, Integer[]> entry : this.map.entrySet())
        {
            Float[] array = new Float[ChunkProfiler.CHUNK_HEIGHT];
            for (int i = 0; i < ChunkProfiler.CHUNK_HEIGHT; i++)
                array[i] = entry.getValue()[i] * 1.0F / this.blocks;
            distribs.put(entry.getKey(), array);
        }
        ProfilingAdapter.write(distribs);
        this.timer.completed();
    }
}
