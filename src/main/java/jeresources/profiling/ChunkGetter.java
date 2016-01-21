package jeresources.profiling;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class ChunkGetter implements Runnable
{
    private static final int GENERATE_SIZE = 7;
    public static final int CHUNKS_PER_RUN = (GENERATE_SIZE - 2) * (GENERATE_SIZE - 2);
    private static final int MAX_CHUNK_POS = (30000000 / 16) - GENERATE_SIZE;

    private final int maxRunCount;
    private final DummyWorld world;
    private final Profiler profiler;
    private final Runnable runnable;
    private int runCount;

    public ChunkGetter(int maxRunCount, WorldServer world, Profiler profiler) {
        this.maxRunCount = maxRunCount;
        this.world = new DummyWorld(world);
        this.world.init();

        this.profiler = profiler;
        this.runnable = new Runnable() {
            @Override
            public void run() {
                Profiler profiler = getProfiler();
                if (getRunCount() < getMaxRunCount())
                {
                    final DummyWorld dummyWorld = getWorld();

                    List<Chunk> chunks = generateChunks(dummyWorld);
                    runCount++;
                    profiler.addChunkProfiler(dummyWorld, chunks);

                    // add the next task to executor thread first so that the world's scheduledTasks
                    // has a chance to process other things like chat input
                    profiler.getExecutor().execute(new Runnable() {
                        @Override
                        public void run() {
                            dummyWorld.addScheduledTask(runnable);
                        }
                    });
                }
                else
                    profiler.getExecutor().shutdown();
            }
        };
    }

    @Override
    public void run() {
        runnable.run();
    }

    private Profiler getProfiler() {
        return profiler;
    }

    private DummyWorld getWorld() {
        return world;
    }

    private int getMaxRunCount() {
        return maxRunCount;
    }

    private int getRunCount() {
        return runCount;
    }

    private static List<Chunk> generateChunks(DummyWorld dummyWorld)
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
                Chunk chunk = dummyWorld.getChunkFromChunkCoords(chunkX + i, chunkZ + j);
                if (i > 0 && i < (GENERATE_SIZE - 1) && j > 0 && j < (GENERATE_SIZE - 1))
                {
                    centerChunks.add(chunk);
                }
            }
        }

        return centerChunks;
    }
}
