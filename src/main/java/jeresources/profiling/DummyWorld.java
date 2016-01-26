package jeresources.profiling;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.BlockPos;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.LongHashMap;
import net.minecraft.util.ReportedException;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.NextTickListEntry;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.EmptyChunk;
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
        super(Minecraft.getMinecraft().getIntegratedServer(), world.getSaveHandler(), world.getWorldInfo(), world.provider.getDimensionId(), world.theProfiler);
        this.provider.registerWorld(this);
        this.chunkProvider = new DummyChunkProvider(this, this.theChunkProviderServer);
    }

    public void clearChunks()
    {
        ((DummyChunkProvider) this.chunkProvider).clearChunks();
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
    public List<NextTickListEntry> func_175712_a(StructureBoundingBox structureBB, boolean p_175712_2_)
    {
        return Collections.emptyList();
    }

    @Override
    public boolean spawnEntityInWorld(Entity entity)
    {
        this.spawnedEntities.add(entity);
        return true;
    }

    @Override
    protected int getRenderDistanceChunks()
    {
        return 0;
    }

    private static class DummyChunkProvider implements IChunkProvider
    {
        private final World dummyWorld;
        private final IChunkProvider realChunkProvider;
        private LongHashMap<Chunk> id2ChunkMap = new LongHashMap<>();
        private boolean allowLoading = true;

        public DummyChunkProvider(DummyWorld dummyWorld, IChunkProvider chunkProvider)
        {
            this.dummyWorld = dummyWorld;
            if (chunkProvider instanceof ChunkProviderServer)
            {
                ChunkProviderServer chunkProviderServer = (ChunkProviderServer) chunkProvider;
                this.realChunkProvider = chunkProviderServer.serverChunkGenerator;
            } else
            {
                this.realChunkProvider = chunkProvider;
            }
        }

        public void clearChunks()
        {
            this.id2ChunkMap = new LongHashMap<>();
        }

        @Override
        public int getLoadedChunkCount()
        {
            return id2ChunkMap.getNumHashElements();
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
        public List<BiomeGenBase.SpawnListEntry> getPossibleCreatures(EnumCreatureType creatureType, BlockPos pos)
        {
            return null;
        }

        @Override
        public BlockPos getStrongholdGen(World worldIn, String structureName, BlockPos position)
        {
            return realChunkProvider.getStrongholdGen(worldIn, structureName, position);
        }

        @Override
        public void populate(IChunkProvider provider, int x, int z)
        {
            allowLoading = false;
            realChunkProvider.populate(this, x, z);
            GameRegistry.generateWorld(x, z, dummyWorld, this, this);
            allowLoading = true;
        }

        @Override
        public boolean func_177460_a(IChunkProvider p_177460_1_, Chunk p_177460_2_, int p_177460_3_, int p_177460_4_)
        {
            // no retrogen of ocean monuments
            return false;
        }

        @Override
        public boolean chunkExists(int x, int z)
        {
            return this.id2ChunkMap.containsItem(ChunkCoordIntPair.chunkXZ2Int(x, z));
        }

        @Override
        public Chunk provideChunk(int x, int z)
        {
            final long chunkKey = ChunkCoordIntPair.chunkXZ2Int(x, z);
            Chunk chunk = this.id2ChunkMap.getValueByKey(chunkKey);
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
                chunk = realChunkProvider.provideChunk(x, z);
            } catch (Throwable throwable)
            {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception generating new chunk");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Chunk to be generated");
                crashreportcategory.addCrashSection("Location", String.format("%d,%d", x, z));
                crashreportcategory.addCrashSection("Generator", realChunkProvider.makeString());
                throw new ReportedException(crashreport);
            }

            this.id2ChunkMap.add(chunkKey, chunk);

            this.allowLoading = false;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.world.ChunkEvent.Load(chunk));
            chunk.populateChunk(this, this, x, z);
            this.allowLoading = true;

            return chunk;
        }

        @Override
        public Chunk provideChunk(BlockPos blockPosIn)
        {
            return this.provideChunk(blockPosIn.getX() >> 4, blockPosIn.getZ() >> 4);
        }

        @Override
        public boolean canSave()
        {
            return false;
        }

        @Override
        public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate)
        {
            iprogressupdate.setDoneWorking();
            return true;
        }

        @Override
        public void saveExtraData()
        {
        }

        @Override
        public boolean unloadQueuedChunks()
        {
            return false;
        }
    }
}
