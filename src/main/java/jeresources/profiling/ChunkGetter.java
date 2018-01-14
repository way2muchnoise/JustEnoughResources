package jeresources.profiling;

import jeresources.util.LogHelper;
import net.minecraft.util.ReportedException;
import net.minecraft.world.WorldServer;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.Chunk;

import java.util.ArrayList;
import java.util.List;

public class ChunkGetter implements Runnable {
    public static final int CHUNKS_PER_RUN = 25;

    private final int maxRunCount;
    private final DummyWorld world;
    private final Runnable runnable;
    private IChunkGetterStrategy strategy;
    private int runCount;

    public ChunkGetter(final int chunkCount, DummyWorld world, final ProfilingExecutor executor) {
        this.maxRunCount = (int) Math.ceil(chunkCount / (float) CHUNKS_PER_RUN);
        this.world = world;

        this.strategy = new ChunkGetterRandom(world);

        this.runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (getRunCount() < getMaxRunCount()) {
                        final DummyWorld dummyWorld = getWorld();

                        List<Chunk> chunks = strategy.generateChunks(dummyWorld);

                        // check if we should switch strategies for dimensions like the end
                        if (strategy instanceof ChunkGetterRandom && areAllChunksEmpty(chunks)) {
                            strategy = new ChunkGetterOrigin(dummyWorld, chunkCount);
                            chunks = strategy.generateChunks(dummyWorld);
                        }

                        runCount++;
                        executor.addChunkProfiler(dummyWorld, chunks);

                        // add the next task to executor thread first so that the world's scheduledTasks
                        // has a chance to process other things like chat input
                        executor.execute(() -> dummyWorld.addScheduledTask(runnable));
                    } else
                        executor.shutdown();
                } catch (ReportedException re) {
                    LogHelper.info("Chunk getting failed: " + re.getMessage());
                    executor.shutdown();
                }
            }
        };
    }

    @Override
    public void run() {
        runnable.run();
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

    private boolean areAllChunksEmpty(List<Chunk> chunks) {
        for (Chunk chunk : chunks) {
            if (chunk.getTopFilledSegment() != 0)
                return false;
        }
        return true;
    }

    private interface IChunkGetterStrategy {
        List<Chunk> generateChunks(DummyWorld dummyWorld);
    }

    private static class ChunkGetterRandom implements IChunkGetterStrategy {
        private static final int GENERATE_SIZE = (int) Math.ceil(Math.sqrt(CHUNKS_PER_RUN)) + 2;
        private final WorldBorder worldBorder;

        public ChunkGetterRandom(WorldServer world) {
            this.worldBorder = world.getWorldBorder();

        }

        @Override
        public List<Chunk> generateChunks(DummyWorld dummyWorld) {
            final int maxChunkPos = (worldBorder.getSize() / 16) - GENERATE_SIZE;
            // load a square of chunks in a random area, and then profile the center ones.
            // worldgen does not populate the chunks that are on the edges.

            final int chunkX = dummyWorld.rand.nextInt(2 * maxChunkPos) - maxChunkPos + (int) worldBorder.getCenterX();
            final int chunkZ = dummyWorld.rand.nextInt(2 * maxChunkPos) - maxChunkPos + (int) worldBorder.getCenterZ();

            return ChunkGetter.centerChunks(dummyWorld, chunkX, chunkZ, GENERATE_SIZE);
        }
    }

    // gets chunks around the origin 0,0
    private static class ChunkGetterOrigin implements IChunkGetterStrategy {
        private static final int GENERATE_SIZE = (int) Math.ceil(Math.sqrt(CHUNKS_PER_RUN)) + 2;

        private final int sideLength;
        private final int minX, maxX;
        private int posX;
        private int posZ;

        public ChunkGetterOrigin(WorldServer worldServer, int chunkCount) {
            this.sideLength = (int) Math.ceil(Math.sqrt(chunkCount));

            WorldBorder worldBorder = worldServer.getWorldBorder();
            this.minX = (int) worldBorder.getCenterX() - this.sideLength / 2;
            this.maxX = (int) worldBorder.getCenterX() + this.sideLength / 2;
            this.posX = this.minX;

            this.posZ = (int) worldBorder.getCenterZ() - this.sideLength / 2;
        }

        @Override
        public List<Chunk> generateChunks(DummyWorld dummyWorld) {
            final int chunkX = this.posX;
            final int chunkZ = this.posZ;

            this.posX += (GENERATE_SIZE - 1);
            if (this.posX > this.maxX) {
                this.posX = this.minX;
                this.posZ += (GENERATE_SIZE - 1);
            }

            return ChunkGetter.centerChunks(dummyWorld, chunkX, chunkZ, GENERATE_SIZE);
        }
    }

    private static List<Chunk> centerChunks(DummyWorld dummyWorld, int chunkX, int chunkZ, int generate_size) {
        final List<Chunk> centerChunks = new ArrayList<>();
        for (int i = 0; i < generate_size; i++) {
            for (int j = 0; j < generate_size; j++) {
                Chunk chunk = dummyWorld.getChunkFromChunkCoords(chunkX + i, chunkZ + j);
                if (i > 0 && i < (generate_size - 1) && j > 0 && j < (generate_size - 1)) {
                    centerChunks.add(chunk);
                }
            }
        }
        return centerChunks;
    }
}
