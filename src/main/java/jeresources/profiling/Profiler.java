package jeresources.profiling;

import jeresources.json.ProfilingAdapter;
import net.minecraft.block.Block;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Profiler
{
    public static String profileChunks(World world, BlockPos origin, int chunks)
    {
        ExecutorService executor = Executors.newCachedThreadPool();
        ConcurrentMap<Block, Integer[]> map = new ConcurrentHashMap<>();
        long start = System.currentTimeMillis();
        int width = (int)Math.round(Math.sqrt(chunks));
        int offset = width / 2;
        int blocks = width * width * ChunkProfiler.CHUNK_SIZE * ChunkProfiler.CHUNK_SIZE; // blocks in one y level

        for (int i = 0; i < width; i++)
        {
            for (int j = 0; j < width; j++)
            {
                executor.execute(new ChunkProfiler(
                        world.getChunkFromBlockCoords(origin.add(
                                (i - offset) * ChunkProfiler.CHUNK_SIZE,
                                0 ,
                                (j - offset) * ChunkProfiler.CHUNK_SIZE)
                        ), map));
            }
        }
        executor.shutdown();
        while (!executor.isTerminated()) {}
        Map<Block, Float[]> distribs = new HashMap<>();
        for (Map.Entry<Block, Integer[]> entry : map.entrySet())
        {
            Float[] array = new Float[ChunkProfiler.CHUNK_HEIGHT];
            for (int i = 0; i < ChunkProfiler.CHUNK_HEIGHT; i++)
                array[i] = entry.getValue()[i] * 1.0F / blocks;
            distribs.put(entry.getKey(), array);
        }
        long end = System.currentTimeMillis();
        ProfilingAdapter.write(distribs);
        return "Completed profiling of " + (blocks * ChunkProfiler.CHUNK_HEIGHT)  + " blocks in " + (end - start)  + " ms saved to blocks.json";
    }
}
