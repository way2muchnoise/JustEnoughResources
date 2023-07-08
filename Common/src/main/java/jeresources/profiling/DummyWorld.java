package jeresources.profiling;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * Dummy world wraps a regular world.
 * It prevents saving new chunks, doing lighting calculations, or spawning entities.
 */
public class DummyWorld extends ServerLevel {
    public List<Entity> spawnedEntities = new ArrayList<>();
    // private CapabilityDispatcher capabilities;

    public DummyWorld(ServerLevel level) {
        // TODO: implement
        // MinecraftServer, Executor, LevelStorageSource.LevelStorageAccess, ServerLevelData, ResourceKey<Level>, LevelStem, ChunkProgressListener, boolean, long, List<CustomSpawner>, boolean, RandomSequences
        super(null, null, null, null, null, null, null, false,0, null, false, null);
        // this.dimension.setWorld(this);
        // this.function = world.getFunctionManager(); // Make sure this is here for a tick between object creation and dummy world init
        // this.capabilities = ForgeEventFactory.gatherCapabilities(DummyWorld.class, this);
    }

    public void clearChunks() {
        // ((DummyChunkProvider) this.chunkProvider).unloadAllChunks();
    }

    @Nullable
    @Override
    public Entity getEntity(int id) {
        return null;
    }

    @Nullable
    @Override
    public MapItemSavedData getMapData(String mapName) {
        return null;
    }

    @Override
    public void setMapData(String p_143305_, MapItemSavedData p_143306_) {
        this.getServer().overworld().getDataStorage().set(p_143305_, p_143306_);
    }

    @Override
    public int getFreeMapId() {
        return 0;
    }

    @Override
    public void destroyBlockProgress(int breakerId, BlockPos pos, int progress) {

    }

    @Override
    public RecipeManager getRecipeManager() {
        return null;
    }

    @Override
    public boolean setBlock(BlockPos pos, BlockState newState, int flags) {
        if (!isOutsideBuildHeight(pos) || !isLoaded(pos)) {
            return false;
        }

        ChunkAccess chunk = getChunk(pos);
        BlockState blockState = chunk.setBlockState(pos, newState, false);
        return blockState != null;
    }

    @Override
    public boolean setBlockAndUpdate(BlockPos pos, BlockState state) {
        return this.setBlock(pos, state, 3);
    }

    @Override
    public void sendBlockUpdated(BlockPos pos, BlockState oldState, BlockState newState, int flags) {

    }

    @Override
    public void playSound(@Nullable Player player, double x, double y, double z, SoundEvent soundIn, SoundSource source, float volume, float pitch) {

    }

    @Override
    public void playSound(@Nullable Player p_217384_1_, Entity p_217384_2_, SoundEvent p_217384_3_, SoundSource p_217384_4_, float p_217384_5_, float p_217384_6_) {

    }

    @Override
    public boolean addFreshEntity(Entity entity) {
        this.spawnedEntities.add(entity);
        return true;
    }

// FORGE STUFF
//    @Nullable
//    @Override
//    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction direction) {
//        return capabilities == null ? null : capabilities.getCapability(capability, direction);
//    }

    @Override
    public void levelEvent(@Nullable Player player, int type, BlockPos pos, int data) {

    }

    private static class DummyChunkSource extends ChunkSource {
        private final Level realLevel;
        private final ChunkSource realChunkSource;
        private boolean allowLoading = true;

        public DummyChunkSource(Level realLevel, ChunkSource serverChunkSource) {
            super();
            this.realLevel = realLevel;
            this.realChunkSource = serverChunkSource;
        }

        @Nullable
        @Override
        public ChunkAccess getChunk(int x, int z, ChunkStatus requiredStatus, boolean load) {
            /*
            final long chunkKey = ChunkPos.asLong(x, z);
            LevelChunk chunk = this.loadedChunks.get(chunkKey);
            if (chunk != null) {
                return chunk;
            }
            if (!allowLoading) {
                return new EmptyChunkJER(dummyWorld, x, z);
            }

            try {
                chunk = realChunkSource.getChunkNow(x, z);
            } catch (Throwable throwable) {
                CrashReport crashreport = CrashReport.forThrowable(throwable, "Exception generating new chunk");
                CrashReportCategory crashreportcategory = crashreport.addCategory("Chunk to be generated");
                crashreportcategory.setDetail("Location", String.format("%d,%d", x, z));
                crashreportcategory.setDetail("Generator", realChunkSource.gatherStats());
                throw new ReportedException(crashreport);
            }

            this.loadedChunks.put(chunkKey, chunk);

            this.allowLoading = false;
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.world.ChunkEvent.Load(chunk));
            chunk.populate(this, this);
            this.allowLoading = true;

            return chunk;
            */
            return null;
        }

        @Override
        public void tick(BooleanSupplier booleanSupplier, boolean bool) {

        }

        @Override
        public String gatherStats() {
            return "Dummy";
        }

        @Override
        public int getLoadedChunksCount() {
            return 0;
        }

        @Override
        public LevelLightEngine getLightEngine() {
            return null;
        }

        @Override
        public BlockGetter getLevel() {
            return null;
        }
    }
}
