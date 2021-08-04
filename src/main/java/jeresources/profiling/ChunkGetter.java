package jeresources.profiling;

import com.mojang.datafixers.util.Either;
import jeresources.util.LogHelper;
import net.minecraft.crash.ReportedException;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.ChunkPrimerWrapper;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.server.ChunkHolder;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.ServerWorldLightManager;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ChunkGetter implements Runnable {
    public static final int CHUNKS_PER_RUN = 25;

    private final int maxRunCount;
    private final Runnable runnable;
    private IChunkGetterStrategy strategy;
    private int runCount;

    public ChunkGetter(final int chunkCount, ServerWorld world, final ProfilingExecutor executor) {
        this.maxRunCount = (int) Math.ceil(chunkCount / (float) CHUNKS_PER_RUN);

        this.strategy = new ChunkGetterRandom(world);

        this.runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    if (getRunCount() < getMaxRunCount()) {

                        List<IChunk> chunks = strategy.generateChunks(world);

                        // check if we should switch strategies for dimensions like the end
                        if (strategy instanceof ChunkGetterRandom && areAllChunksEmpty(chunks)) {
                            strategy = new ChunkGetterOrigin(world, chunkCount);
                            chunks = strategy.generateChunks(world);
                        }

                        runCount++;
                        executor.addChunkProfiler(world, chunks);

                        // add the next task to executor thread first so that the world's scheduledTasks
                        // has a chance to process other things like chat input
                        executor.execute(() -> world.getServer().addTickable(runnable));
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

    private boolean areAllChunksEmpty(List<IChunk> chunks) {
        for (IChunk chunk : chunks) {
            if (chunk.getHighestSectionPosition() != 0)
                return false;
        }
        return true;
    }

    private interface IChunkGetterStrategy {
        List<IChunk> generateChunks(ServerWorld world);
    }

    private static class ChunkGetterRandom implements IChunkGetterStrategy {
        private static final int GENERATE_SIZE = (int) Math.ceil(Math.sqrt(CHUNKS_PER_RUN)) + 2;
        private final ChunkGenerator chunkGenerator;

        public ChunkGetterRandom(ServerWorld world) {
            this.chunkGenerator = world.getChunkSource().getGenerator().withSeed(world.random.nextLong());
        }

        @Override
        public List<IChunk> generateChunks(ServerWorld world) {
            WorldBorder worldBorder = world.getWorldBorder();
            final int maxChunkPos = (int)(worldBorder.getSize() / 16) - GENERATE_SIZE;
            // load a square of chunks in a random area, and then profile the center ones.
            // worldgen does not populate the chunks that are on the edges.

            final int chunkX = world.random.nextInt(2 * maxChunkPos) - maxChunkPos + (int) worldBorder.getCenterX();
            final int chunkZ = world.random.nextInt(2 * maxChunkPos) - maxChunkPos + (int) worldBorder.getCenterZ();

            return ChunkGetter.centerChunks(world, this.chunkGenerator, chunkX, chunkZ, GENERATE_SIZE);
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

        public ChunkGetterOrigin(ServerWorld world, int chunkCount) {
            this.chunkGenerator = world.getChunkSource().getGenerator().withSeed(world.random.nextLong());

            this.sideLength = (int) Math.ceil(Math.sqrt(chunkCount));

            WorldBorder worldBorder = world.getWorldBorder();
            this.minX = (int) worldBorder.getCenterX() - this.sideLength / 2;
            this.maxX = (int) worldBorder.getCenterX() + this.sideLength / 2;
            this.posX = this.minX;

            this.posZ = (int) worldBorder.getCenterZ() - this.sideLength / 2;
        }

        @Override
        public List<IChunk> generateChunks(ServerWorld world) {
            final int chunkX = this.posX;
            final int chunkZ = this.posZ;

            this.posX += (GENERATE_SIZE - 1);
            if (this.posX > this.maxX) {
                this.posX = this.minX;
                this.posZ += (GENERATE_SIZE - 1);
            }

            return ChunkGetter.centerChunks(world, this.chunkGenerator, chunkX, chunkZ, GENERATE_SIZE);
        }
    }

    private static List<IChunk> centerChunks(ServerWorld world, ChunkGenerator chunkGenerator, int chunkX, int chunkZ, int generate_size) {
        final List<IChunk> centerChunks = new LinkedList<>();

        for (int i = 0; i < generate_size; i++) {
            for (int j = 0; j < generate_size; j++) {
                if (i > 0 && i < (generate_size - 1) && j > 0 && j < (generate_size - 1)) {
                    centerChunks.add(new ChunkPrimerWrapper(new EmptyChunkJER(world, chunkX + i, chunkZ + j)));
                }
            }
        }



        // TODO - do I need to recurse over all parent states first ?
        ChunkStatus.FEATURES.generate(world, chunkGenerator,
            world.getStructureManager(), (ServerWorldLightManager) world.getLightEngine(),
            iChunk -> iChunk.getStatus().isOrAfter(ChunkStatus.FEATURES) ? CompletableFuture.completedFuture(Either.left(iChunk)) : ChunkHolder.UNLOADED_CHUNK_FUTURE,
            centerChunks);

        return centerChunks;
    }
}
