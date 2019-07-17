package jeresources.profiling;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.block.state.IBlockState;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.*;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Dummy world wraps a regular world.
 * It prevents saving new chunks, doing lighting calculations, or spawning entities.
 */
public class DummyWorld extends WorldServer {
    public List<Entity> spawnedEntities = new ArrayList<>();
    private CapabilityDispatcher capabilities;

    public DummyWorld(WorldServer world) {
        super(world.getServer(), world.getSaveHandler(), world.getMapStorage(), world.getWorldInfo(), world.getDimension().getType(), world.profiler);
        this.dimension.setWorld(this);
        this.chunkProvider = new DummyChunkProvider(this, this.getChunkProvider());
        // this.function = world.getFunctionManager(); // Make sure this is here for a tick between object creation and dummy world init
        this.capabilities = ForgeEventFactory.gatherCapabilities(DummyWorld.class, this);
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

        Chunk chunk = getChunk(pos);
        IBlockState blockState = chunk.setBlockState(pos, newState, false);
        return blockState != null;
    }

    @Override
    public boolean setBlockState(BlockPos pos, IBlockState state) {
        return this.setBlockState(pos, state, 3);
    }


    @Override
    public boolean spawnEntity(Entity entity) {
        this.spawnedEntities.add(entity);
        return true;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capabilities == null ? null : capabilities.getCapability(capability, facing);
    }

    private static class DummyChunkProvider extends ChunkProviderServer implements IChunkProvider, IChunkGenerator {
        private final World dummyWorld;
        private final IChunkGenerator realChunkGenerator;
        private final IChunkProvider realChunkProvider;
        private boolean allowLoading = true;

        public DummyChunkProvider(DummyWorld dummyWorld, ChunkProviderServer chunkProviderServer) {
            super(dummyWorld, chunkProviderServer.chunkLoader, chunkProviderServer.chunkGenerator, dummyWorld.getServer());
            this.dummyWorld = dummyWorld;
            this.realChunkGenerator = chunkProviderServer.chunkGenerator;
            this.realChunkProvider = chunkProviderServer;
        }

        @Override
        public String makeString() {
            return "Dummy";
        }

        @Override
        public void makeBase(IChunk chunk) {

        }

        @Override
        public void carve(WorldGenRegion worldGenRegion, GenerationStage.Carving carving) {

        }

        @Override
        public void decorate(WorldGenRegion worldGenRegion) {

        }

        @Override
        public void spawnMobs(WorldGenRegion worldGenRegion) {

        }

        @Override
        public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos) {
            return null;
        }

        @Override
        public IChunkGenSettings getSettings() {
            return null;
        }

        @Override
        public BiomeProvider getBiomeProvider() {
            return null;
        }

        @Override
        public long getSeed() {
            return 0;
        }

        @Override
        public int getGroundHeight() {
            return 0;
        }

        @Override
        public int getMaxHeight() {
            return 0;
        }

        @Override
        public Long2ObjectMap<LongSet> getStructurePositionToReferenceMap(Structure structure) {
            return null;
        }

        @Override
        public Long2ObjectMap<StructureStart> getStructureReferenceToStartMap(Structure structure) {
            return null;
        }

        @Nullable
        @Override
        public IFeatureConfig getStructureConfig(Biome biome, Structure structure) {
            return null;
        }

        @Override
        public boolean hasStructure(Biome biome, Structure structure) {
            return false;
        }

        /*
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
            return this.loadedChunks.get(chunkKey);
        }
        */

        public void unloadAllChunks() {
            this.loadedChunks.clear();
        }

        @Override
        public boolean saveChunks(boolean all) {
            return true;
        }

        /*
        @Override
        public Chunk generateChunk(int x, int z) {
            final long chunkKey = ChunkPos.asLong(x, z);
            Chunk chunk = this.loadedChunks.get(chunkKey);
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

            this.loadedChunks.put(chunkKey, chunk);

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

        */
    }
}
