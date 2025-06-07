package jeresources.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientChunkCache;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.scores.Scoreboard;
import org.jetbrains.annotations.Nullable;

import java.util.function.BooleanSupplier;

public class FakeClientLevel extends ClientLevel {
    public static final ClientLevelData clientLevelData = new ClientLevelData(Difficulty.NORMAL, false, false);

    // private CapabilityDispatcher capabilities;

    public FakeClientLevel() {
        //ClientPacketListener, ClientLevelData, ResourceKey<Level>, Holder<DimensionType>, int, int, LevelRenderer levelRenderer, boolean, long, int
        super(null, clientLevelData, Level.OVERWORLD, DimensionHelper.getType(BuiltinDimensionTypes.OVERWORLD), 0, 0, Minecraft.getInstance().levelRenderer, false, 1234567, 0);
        // this.capabilities = ForgeEventFactory.gatherCapabilities(FakeClientLevel.class, this);
    }

// FORGE STUFF
//    @Nullable
//    @Override
//    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
//        return capabilities == null ? null : capabilities.getCapability(capability, facing);
//    }

    @Override
    public void sendBlockUpdated(BlockPos pos, BlockState oldState, BlockState newState, int flags) {

    }

    @Override
    public void playSound(@Nullable Player player, double x, double y, double z, SoundEvent soundIn, SoundSource source, float volume, float pitch) {

    }

    @Override
    public void playSound(@Nullable Player p_184133_1_, BlockPos p_184133_2_, SoundEvent p_184133_3_, SoundSource source, float p_184133_5_, float p_184133_6_) {

    }

    @Nullable
    @Override
    public Entity getEntity(int p_73045_1_) {
        return null;
    }

    @Override
    public ClientChunkCache getChunkSource() {
        return new ClientChunkCache(this, 0) {
            @Nullable
            @Override
            public LevelChunk getChunk(int x, int z, ChunkStatus chunkStatus, boolean requireChunk) {
                return super.getChunk(x, z, chunkStatus, requireChunk);
            }

            @Override
            public void tick(BooleanSupplier booleanSupplier, boolean bool) {

            }

            @Override
            public String gatherStats() {
                return "emptychunkprovider";
            }

            @Override
            public LevelLightEngine getLightEngine() {
                return null;
            }

            @Override
            public BlockGetter getLevel() {
                return FakeClientLevel.this;
            }
        };
    }

    @Nullable
    @Override
    public MapItemSavedData getMapData(MapId mapId) {
        return super.getMapData(mapId);
    }

    @Override
    public void setMapData(MapId mapId, MapItemSavedData mapData) {
        super.setMapData(mapId, mapData);
    }

    @Override
    public MapId getFreeMapId() {
        return new MapId(0);
    }

    @Override
    public void destroyBlockProgress(int breakerId, BlockPos pos, int progress) {

    }

    @Override
    public Scoreboard getScoreboard() {
        return null;
    }

    @Override
    public RecipeManager recipeAccess() {
        return null;
    }

    @Override
    public boolean hasChunk(int p_217354_1_, int p_217354_2_) {
        return false;
    }

    @Override
    public void levelEvent(@Nullable Player player, int type, BlockPos pos, int data) {

    }
}
