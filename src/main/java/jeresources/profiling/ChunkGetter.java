package jeresources.profiling;

import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

public class ChunkGetter implements Runnable
{
    private static final int GENERATE_SIZE = 7;
    public static final int CHUNKS_PER_RUN = (GENERATE_SIZE - 2) * (GENERATE_SIZE - 2);
    private static final int MAX_CHUNK_POS = (30000000 / 16) - GENERATE_SIZE;

    private DummyWorld dummyWorld;
    private Profiler profiler;

    public ChunkGetter(DummyWorld dummyWorld, Profiler profiler)
    {
        this.dummyWorld = dummyWorld;
        this.profiler = profiler;
    }

    @Override
    public void run()
    {
        // load a square of chunks in a random area, and then profile the center ones.
        // worldgen does not populate the chunks that are on the edges.

        int chunkX = dummyWorld.rand.nextInt(2 * MAX_CHUNK_POS) - MAX_CHUNK_POS;
        int chunkZ = dummyWorld.rand.nextInt(2 * MAX_CHUNK_POS) - MAX_CHUNK_POS;

        List<Chunk> centerChunks = new ArrayList<>();
        for (int i = 0; i < GENERATE_SIZE; i++)
        {
            for (int j = 0; j < GENERATE_SIZE; j++)
            {
                Chunk chunk = this.dummyWorld.getChunkFromChunkCoords(chunkX + i, chunkZ + j);
                if (i > 0 && i < (GENERATE_SIZE - 1) && j > 0 && j < (GENERATE_SIZE - 1))
                {
                    centerChunks.add(chunk);
                }
            }
        }

        this.profiler.addChunkProfiler(dummyWorld, centerChunks);
    }
}
