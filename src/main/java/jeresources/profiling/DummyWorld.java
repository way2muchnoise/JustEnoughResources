package jeresources.profiling;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.ITagCollectionSupplier;
import net.minecraft.tags.NetworkTagManager;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.chunk.listener.IChunkStatusListener;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.lighting.WorldLightManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.ISpecialSpawner;
import net.minecraft.world.storage.IServerWorldInfo;
import net.minecraft.world.storage.MapData;
import net.minecraft.world.storage.SaveFormat;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dummy world wraps a regular world.
 * It prevents saving new chunks, doing lighting calculations, or spawning entities.
 */
public class DummyWorld extends ServerWorld {
    public List<Entity> spawnedEntities = new ArrayList<>();
    private CapabilityDispatcher capabilities;

    public DummyWorld(ServerWorld world) {
        // TODO: implement
        //MinecraftServer, Executor, SaveFormat.LevelSave, IServerWorldInfo, RegistryKey<World>, DimensionType, IChunkStatusListener, ChunkGenerator, boolean, long, List<ISpecialSpawner>, boolean
        super(null, null, null, null, null, null, null, null, false,0, null, false);
        // this.dimension.setWorld(this);
        // this.function = world.getFunctionManager(); // Make sure this is here for a tick between object creation and dummy world init
        this.capabilities = ForgeEventFactory.gatherCapabilities(DummyWorld.class, this);
    }

    public void clearChunks() {
        // ((DummyChunkProvider) this.chunkProvider).unloadAllChunks();
    }

    @Override
    public Entity getEntityByID(int i) {
        return null;
    }

    @Nullable
    @Override
    public MapData getMapData(String mapName) {
        return null;
    }

    @Override
    public void registerMapData(MapData mapDataIn) {

    }

    @Override
    public int getNextMapId() {
        return 0;
    }

    @Override
    public void sendBlockBreakProgress(int breakerId, BlockPos pos, int progress) {

    }

    @Override
    public RecipeManager getRecipeManager() {
        return null;
    }

    @Override
    public ITagCollectionSupplier getTags() {
        return null;
    }

    @Override
    public boolean setBlockState(BlockPos pos, BlockState newState, int flags) {
        if (!isValid(pos) || !isBlockLoaded(pos)) {
            return false;
        }

        IChunk chunk = getChunk(pos);
        BlockState blockState = chunk.setBlockState(pos, newState, false);
        return blockState != null;
    }

    @Override
    public boolean setBlockState(BlockPos pos, BlockState state) {
        return this.setBlockState(pos, state, 3);
    }

    @Override
    public void notifyBlockUpdate(BlockPos pos, BlockState oldState, BlockState newState, int flags) {

    }

    @Override
    public void playSound(@Nullable PlayerEntity player, double x, double y, double z, SoundEvent soundIn, SoundCategory category, float volume, float pitch) {

    }

    @Override
    public void playMovingSound(@Nullable PlayerEntity p_217384_1_, Entity p_217384_2_, SoundEvent p_217384_3_, SoundCategory p_217384_4_, float p_217384_5_, float p_217384_6_) {

    }

    @Override
    public boolean addEntity(Entity entity) {
        this.spawnedEntities.add(entity);
        return true;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction direction) {
        return capabilities == null ? null : capabilities.getCapability(capability, direction);
    }

    @Override
    public void playEvent(@Nullable PlayerEntity player, int type, BlockPos pos, int data) {

    }

    private static class DummyChunkProvider extends AbstractChunkProvider {
        private final World realWorld;
        private final AbstractChunkProvider realChunkProvider;
        private boolean allowLoading = true;

        public DummyChunkProvider(World realWorld, AbstractChunkProvider chunkProviderServer) {
            super();
            this.realWorld = realWorld;

            this.realChunkProvider = chunkProviderServer;
        }

        @Nullable
        @Override
        public IChunk getChunk(int x, int z, ChunkStatus requiredStatus, boolean load) {
            /*
            final long chunkKey = ChunkPos.asLong(x, z);
            Chunk chunk = this.loadedChunks.get(chunkKey);
            if (chunk != null) {
                return chunk;
            }
            if (!allowLoading) {
                return new EmptyChunkJER(dummyWorld, x, z);
            }

            try {
                chunk = realChunkProvider.generateChunk(x, z);
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.makeCrashReport(throwable, "Exception generating new chunk");
                CrashReportCategory crashreportcategory = crashreport.makeCategory("Chunk to be generated");
                crashreportcategory.addDetail("Location", String.format("%d,%d", x, z));
                crashreportcategory.addDetail("Generator", realChunkProvider.makeString());
                throw new ReportedException(crashreport);
            }

            this.loadedChunks.put(chunkKey, chunk);

            this.allowLoading = false;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.world.ChunkEvent.Load(chunk));
            chunk.populate(this, this);
            this.allowLoading = true;

            return chunk;*/
            return null;
        }

        @Override
        public boolean canTick(BlockPos pos) {
            return false;
        }

        @Override
        public String makeString() {
            return "Dummy";
        }

        @Override
        public WorldLightManager getLightManager() {
            return null;
        }

        @Override
        public IBlockReader getWorld() {
            return null;
        }
    }
}
