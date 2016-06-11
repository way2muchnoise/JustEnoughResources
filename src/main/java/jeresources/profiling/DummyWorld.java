package jeresources.profiling;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
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
import net.minecraft.world.chunk.EmptyChunk;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dummy world wraps a regular world.
 * It prevents saving new chunks, doing lighting calculations, or spawning entities.
 */
public class DummyWorld extends WorldServer
{
    public List<Entity> spawnedEntities = new ArrayList<>();

    public DummyWorld(WorldServer world)
    {
        super(Minecraft.getMinecraft().getIntegratedServer(), world.getSaveHandler(), world.getWorldInfo(), world.provider.getDimensionType().getId(), world.theProfiler);
        this.provider.registerWorld(this);
        this.chunkProvider = new DummyChunkProvider(this, this.getChunkProvider());
    }

    public void clearChunks()
    {
        ((DummyChunkProvider) this.chunkProvider).unloadAllChunks();
    }

    @Override
    public Entity getEntityByID(int i)
    {
        return null;
    }

    /**
     * Check if the given BlockPos has valid coordinates
     */
    private boolean isValid(BlockPos pos)
    {
        return pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000 && pos.getY() >= 0 && pos.getY() < 256;
    }

    @Override
    public boolean setBlockState(BlockPos pos, IBlockState newState, int flags)
    {
        if (!isValid(pos))
        {
            return false;
        }

        Chunk chunk = getChunkFromBlockCoords(pos);
        IBlockState blockState = chunk.setBlockState(pos, newState);
        return blockState != null;
    }

    @Override
    public boolean setBlockState(BlockPos pos, IBlockState state)
    {
        return this.setBlockState(pos, state, 3);
    }

    @Override
    public void scheduleBlockUpdate(BlockPos pos, Block blockIn, int delay, int priority)
    {

    }

    @Override
    public void updateBlockTick(BlockPos pos, Block blockIn, int delay, int priority)
    {

    }

    @Override
    public boolean tickUpdates(boolean p_72955_1_)
    {
        return false;
    }

    @Override
    public List<NextTickListEntry> getPendingBlockUpdates(StructureBoundingBox structureBB, boolean p_175712_2_)
    {
        return Collections.emptyList();
    }

    @Override
    public boolean spawnEntityInWorld(Entity entity)
    {
        this.spawnedEntities.add(entity);
        return true;
    }

    private static class DummyChunkProvider extends ChunkProviderServer implements IChunkProvider, IChunkGenerator
    {
        private final World dummyWorld;
        private final IChunkGenerator realChunkGenerator;
        private final IChunkProvider realChunkProvider;
        private boolean allowLoading = true;

        public DummyChunkProvider(DummyWorld dummyWorld, ChunkProviderServer chunkProviderServer)
        {
            super(dummyWorld, chunkProviderServer.chunkLoader, chunkProviderServer.chunkGenerator);
            this.dummyWorld = dummyWorld;
            this.realChunkGenerator = chunkProviderServer.chunkGenerator;
            this.realChunkProvider = chunkProviderServer;
        }

        @Override
        public void recreateStructures(Chunk p_180514_1_, int p_180514_2_, int p_180514_3_)
        {
            // no retrogen
        }

        @Override
        public String makeString()
        {
            return "Dummy";
        }

        @Override
        public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
        {
            return null;
        }

        @Override
        public void populate(int x, int z)
        {
            allowLoading = false;
            realChunkGenerator.populate(x, z);
            GameRegistry.generateWorld(x, z, dummyWorld, this, this);
            allowLoading = true;
        }

        @Override
        public Chunk getLoadedChunk(int x, int z)
        {
            final long chunkKey = ChunkPos.chunkXZ2Int(x, z);
            return this.id2ChunkMap.get(chunkKey);
        }

        @Override
        public void unloadAllChunks()
        {
            this.id2ChunkMap.clear();
        }

        @Override
        public boolean saveChunks(boolean p_186027_1_)
        {
            return true;
        }

        @Override
        public Chunk provideChunk(int x, int z)
        {
            final long chunkKey = ChunkPos.chunkXZ2Int(x, z);
            Chunk chunk = this.id2ChunkMap.get(chunkKey);
            if (chunk != null)
            {
                return chunk;
            }
            if (!allowLoading)
            {
                return new EmptyChunk(dummyWorld, x, z);
            }

            try
            {
                chunk = realChunkGenerator.provideChunk(x, z);
            } catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception generating new chunk");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Chunk to be generated");
                crashreportcategory.addCrashSection("Location", String.format("%d,%d", x, z));
                crashreportcategory.addCrashSection("Generator", realChunkProvider.makeString());
                throw new ReportedException(crashreport);
            }

            this.id2ChunkMap.put(chunkKey, chunk);

            this.allowLoading = false;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.world.ChunkEvent.Load(chunk));
            chunk.populateChunk(this, this);
            this.allowLoading = true;

            return chunk;
        }

        @Override
        public boolean generateStructures(Chunk chunkIn, int x, int z)
        {
            return false;
        }

        @Override
        public boolean unloadQueuedChunks()
        {
            return false;
        }
    }
}
