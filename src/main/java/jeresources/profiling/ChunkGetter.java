package jeresources.profiling;

import com.mojang.datafixers.util.Either;
import jeresources.util.LogHelper;
import net.minecraft.ReportedException;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ThreadedLevelLightEngine;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ChunkGetter implements Runnable {
    public static final int CHUNKS_PER_RUN = 25;

    private final int maxRunCount;
    private final Runnable runnable;
    private IChunkGetterStrategy strategy;
    private int runCount;

    public ChunkGetter(final int chunkCount, ServerLevel level, final ProfilingExecutor executor) {
        this.maxRunCount = (int) Math.ceil(chunkCount / (float) CHUNKS_PER_RUN);

        this.strategy = new ChunkGetterRandom(level);

        this.runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (getRunCount() < getMaxRunCount()) {

                        List<ChunkAccess> chunks = strategy.generateChunks(level);

                        // check if we should switch strategies for dimensions like the end
                        if (strategy instanceof ChunkGetterRandom && areAllChunksEmpty(chunks)) {
                            strategy = new ChunkGetterOrigin(level, chunkCount);
                            chunks = strategy.generateChunks(level);
                        }

                        runCount++;
                        executor.addChunkProfiler(level, chunks);

                        // add the next task to executor thread first so that the world's scheduledTasks
                        // has a chance to process other things like chat input
                        executor.execute(() -> level.getServer().addTickable(runnable));
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

    private int getMaxRunCount() {
        return maxRunCount;
    }

    private int getRunCount() {
        return runCount;
    }

    private boolean areAllChunksEmpty(List<ChunkAccess> chunks) {
        for (ChunkAccess chunk : chunks) {
            if (chunk.getHighestSectionPosition() != 0)
                return false;
        }
        return true;
    }

    private interface IChunkGetterStrategy {
        List<ChunkAccess> generateChunks(ServerLevel serverLevel);
    }

    private static class ChunkGetterRandom implements IChunkGetterStrategy {
        private static final int GENERATE_SIZE = (int) Math.ceil(Math.sqrt(CHUNKS_PER_RUN)) + 2;
        private final ChunkGenerator chunkGenerator;

        public ChunkGetterRandom(ServerLevel level) {
            this.chunkGenerator = level.getChunkSource().getGenerator().withSeed(level.random.nextLong());
        }

        @Override
        public List<ChunkAccess> generateChunks(ServerLevel level) {
            WorldBorder worldBorder = level.getWorldBorder();
            final int maxChunkPos = (int)(worldBorder.getSize() / 16) - GENERATE_SIZE;
            // load a square of chunks in a random area, and then profile the center ones.
            // worldgen does not populate the chunks that are on the edges.

            final int chunkX = level.random.nextInt(2 * maxChunkPos) - maxChunkPos + (int) worldBorder.getCenterX();
            final int chunkZ = level.random.nextInt(2 * maxChunkPos) - maxChunkPos + (int) worldBorder.getCenterZ();

            return ChunkGetter.centerChunks(level, this.chunkGenerator, chunkX, chunkZ, GENERATE_SIZE);
        }
    }

    // gets chunks around the origin 0,0
    private static class ChunkGetterOrigin implements IChunkGetterStrategy {
        private static final int GENERATE_SIZE = (int) Math.ceil(Math.sqrt(CHUNKS_PER_RUN)) + 2;
        private final ChunkGenerator chunkGenerator;

        private final int sideLength;
        private final int minX, maxX;
        private int posX;
        private int posZ;

        public ChunkGetterOrigin(ServerLevel level, int chunkCount) {
            this.chunkGenerator = level.getChunkSource().getGenerator().withSeed(level.random.nextLong());

            this.sideLength = (int) Math.ceil(Math.sqrt(chunkCount));

            WorldBorder worldBorder = level.getWorldBorder();
            this.minX = (int) worldBorder.getCenterX() - this.sideLength / 2;
            this.maxX = (int) worldBorder.getCenterX() + this.sideLength / 2;
            this.posX = this.minX;

            this.posZ = (int) worldBorder.getCenterZ() - this.sideLength / 2;
        }

        @Override
        public List<ChunkAccess> generateChunks(ServerLevel level) {
            final int chunkX = this.posX;
            final int chunkZ = this.posZ;

            this.posX += (GENERATE_SIZE - 1);
            if (this.posX > this.maxX) {
                this.posX = this.minX;
                this.posZ += (GENERATE_SIZE - 1);
            }

            return ChunkGetter.centerChunks(level, this.chunkGenerator, chunkX, chunkZ, GENERATE_SIZE);
        }
    }

    private static List<ChunkAccess> centerChunks(ServerLevel level, ChunkGenerator chunkGenerator, int chunkX, int chunkZ, int generate_size) {
        final List<ChunkAccess> centerChunks = new LinkedList<>();

        for (int i = 0; i < generate_size; i++) {
            for (int j = 0; j < generate_size; j++) {
                if (i > 0 && i < (generate_size - 1) && j > 0 && j < (generate_size - 1)) {
                    centerChunks.add(new EmptyChunkJER(level, chunkX + i, chunkZ + j));
                }
            }
        }



        /*/ TODO - do I need to recurse over all parent states first ?
        ChunkStatus.FEATURES.generate(level.getServer(), level, chunkGenerator,
            level.getStructureManager(), (ThreadedLevelLightEngine) level.getLightEngine(),
            iChunk -> iChunk.getStatus().isOrAfter(ChunkStatus.FEATURES) ? CompletableFuture.completedFuture(Either.left(iChunk)) : ChunkHolder.UNLOADED_CHUNK_FUTURE,
            centerChunks);
         /*/

        return centerChunks;
    }
}
