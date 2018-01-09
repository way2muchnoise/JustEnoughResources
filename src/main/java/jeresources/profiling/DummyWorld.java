package jeresources.profiling;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.ReportedException;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dummy world wraps a regular world.
 * It prevents saving new chunks, doing lighting calculations, or spawning entities.
 */
public class DummyWorld extends WorldServer {
    public List<Entity> spawnedEntities = new ArrayList<>();

    public DummyWorld(WorldServer world, int dimId) {
        super(Minecraft.getMinecraft().getIntegratedServer(), world.getSaveHandler(), world.getWorldInfo(), dimId, world.profiler);
        this.provider.setWorld(this);
        this.chunkProvider = new DummyChunkProvider(this, this.getChunkProvider());
        this.functionManager = world.getFunctionManager(); // Make sure this is here for a tick between object creation and dummy world init
    }

    public void clearChunks() {
        ((DummyChunkProvider) this.chunkProvider).unloadAllChunks();
    }

    @Override
    public Entity getEntityByID(int i) {
        return null;
    }

    @Override
    public boolean setBlockState(BlockPos pos, IBlockState newState, int flags) {
        if (!isValid(pos) || !isBlockLoaded(pos)) {
            return false;
        }

        Chunk chunk = getChunkFromBlockCoords(pos);
        IBlockState blockState = chunk.setBlockState(pos, newState);
        return blockState != null;
    }

    @Override
    public boolean setBlockState(BlockPos pos, IBlockState state) {
        return this.setBlockState(pos, state, 3);
    }

    @Override
    public void scheduleBlockUpdate(BlockPos pos, Block blockIn, int delay, int priority) {

    }

    @Override
    public void updateBlockTick(BlockPos pos, Block blockIn, int delay, int priority) {

    }

    @Override
    public boolean tickUpdates(boolean p_72955_1_) {
        return false;
    }

    @Override
    public List<NextTickListEntry> getPendingBlockUpdates(StructureBoundingBox structureBB, boolean p_175712_2_) {
        return Collections.emptyList();
    }

    @Override
    public boolean spawnEntity(Entity entity) {
        this.spawnedEntities.add(entity);
        return true;
    }

    @Override
    public void tick() {

    }

    private static class DummyChunkProvider extends ChunkProviderServer implements IChunkProvider, IChunkGenerator {
        private final World dummyWorld;
        private final IChunkGenerator realChunkGenerator;
        private final IChunkProvider realChunkProvider;
        private boolean allowLoading = true;

        public DummyChunkProvider(DummyWorld dummyWorld, ChunkProviderServer chunkProviderServer) {
            super(dummyWorld, chunkProviderServer.chunkLoader, chunkProviderServer.chunkGenerator);
            this.dummyWorld = dummyWorld;
            this.realChunkGenerator = chunkProviderServer.chunkGenerator;
            this.realChunkProvider = chunkProviderServer;
        }

        @Override
        public void recreateStructures(Chunk chunkIn, int x, int z) {
            // no retrogen
        }

        @Override
        public boolean isInsideStructure(World worldIn, String structureName, BlockPos pos) {
            return false;
        }

        @Override
        public String makeString() {
            return "Dummy";
        }

        @Override
        public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
            return null;
        }

        @Override
        public void populate(int x, int z) {
            allowLoading = false;
            realChunkGenerator.populate(x, z);
            GameRegistry.generateWorld(x, z, dummyWorld, this, this);
            allowLoading = true;
        }

        @Override
        public Chunk getLoadedChunk(int x, int z) {
            final long chunkKey = ChunkPos.asLong(x, z);
            return this.id2ChunkMap.get(chunkKey);
        }

        public void unloadAllChunks() {
            this.id2ChunkMap.clear();
        }

        @Override
        public boolean saveChunks(boolean all) {
            return true;
        }

        @Override
        public Chunk generateChunk(int x, int z) {
            final long chunkKey = ChunkPos.asLong(x, z);
            Chunk chunk = this.id2ChunkMap.get(chunkKey);
            if (chunk != null) {
                return chunk;
            }
            if (!allowLoading) {
                return new EmptyChunkJER(dummyWorld, x, z);
            }

            try {
                chunk = realChunkGenerator.generateChunk(x, z);
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception generating new chunk");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Chunk to be generated");
                crashreportcategory.addCrashSection("Location", String.format("%d,%d", x, z));
                crashreportcategory.addCrashSection("Generator", realChunkProvider.makeString());
                throw new ReportedException(crashreport);
            }

            this.id2ChunkMap.put(chunkKey, chunk);

            this.allowLoading = false;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.world.ChunkEvent.Load(chunk));
            chunk.populate(this, this);
            this.allowLoading = true;

            return chunk;
        }

        @Override
        public boolean generateStructures(Chunk chunkIn, int x, int z) {
            return false;
        }

        @Override
        public boolean tick() {
            return false;
        }
    }
}
