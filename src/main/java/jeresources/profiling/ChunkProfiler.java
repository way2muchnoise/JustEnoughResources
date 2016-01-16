package jeresources.profiling;

import net.minecraft.block.Block;
import net.minecraft.world.chunk.Chunk;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

public class ChunkProfiler implements Runnable
{
    private final ProfilingTimer timer;
    private final Chunk chunk;
    private ConcurrentMap<Block, Integer[]> map;
    public static final int CHUNK_SIZE = 16;
    public static final int CHUNK_HEIGHT = 256;

    public ChunkProfiler(Chunk chunk, ConcurrentMap<Block, Integer[]> map, ProfilingTimer timer)
    {
        this.chunk = chunk;
        this.map = map;
        this.timer = timer;
    }

    @Override
    public void run()
    {
        timer.startChunk();
        Map<Block, Integer[]> temp = new HashMap<>();
        for (int y = 0; y < CHUNK_HEIGHT; y++)
            for (int i = 0; i < CHUNK_SIZE; i++)
                for (int j = 0; j < CHUNK_SIZE; j++)
                {
                    Block block = this.chunk.getBlock(i, y, j);
                    Integer[] array = temp.get(block);
                    if (array == null)
                    {
                        array = new Integer[CHUNK_HEIGHT];
                        Arrays.fill(array, 0);
                    }
                    array[y]++;
                    temp.put(block, array);
                }
        for (Map.Entry<Block, Integer[]> entry : temp.entrySet())
        {
            Integer[] array = this.map.get(entry.getKey());
            if (array == null)
            {
                array = new Integer[CHUNK_HEIGHT];
                Arrays.fill(array, 0);
            }
            for (int i = 0; i < CHUNK_HEIGHT; i++)
                array[i] += entry.getValue()[i];
            this.map.put(entry.getKey(), array);
        }
        timer.endChunk();
    }
}
