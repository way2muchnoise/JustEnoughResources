package jeresources.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientChunkProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.profiler.EmptyProfiler;
import net.minecraft.profiler.IProfiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.tags.ITagCollectionSupplier;
import net.minecraft.tags.NetworkTagManager;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.lighting.WorldLightManager;
import net.minecraft.world.storage.MapData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;

public class FakeClientWorld extends ClientWorld {
    public static final ClientWorldInfo clientWorldInfo = new ClientWorldInfo(Difficulty.NORMAL, false, false);

    private CapabilityDispatcher capabilities;

    public FakeClientWorld() {
        //ClientPlayNetHandler, ClientWorld.ClientWorldInfo, RegistryKey<World>, DimensionType , int, Supplier<IProfiler>, WorldRenderer, boolean, long
        super(null, clientWorldInfo, World.OVERWORLD, DimensionHelper.getType(DimensionType.OVERWORLD), 0, () -> EmptyProfiler.INSTANCE, Minecraft.getInstance().worldRenderer, false, 1234567);
        this.capabilities = ForgeEventFactory.gatherCapabilities(FakeClientWorld.class, this);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable Direction facing) {
        return capabilities == null ? null : capabilities.getCapability(capability, facing);
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

    @Nullable
    @Override
    public Entity getEntityByID(int id) {
        return null;
    }

    @Override
    public ClientChunkProvider getChunkProvider() {
        return new ClientChunkProvider(this, 0) {
            @Nullable
            @Override
            public Chunk getChunk(int i, int i1, ChunkStatus chunkStatus, boolean b) {
                return null;
            }

            @Override
            public void tick(BooleanSupplier booleanSupplier) {

            }

            @Override
            public String makeString() {
                return "emptychunkprovider";
            }

            @Override
            public WorldLightManager getLightManager() {
                return null;
            }

            @Override
            public IBlockReader getWorld() {
                return FakeClientWorld.this;
            }
        };
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
    public Scoreboard getScoreboard() {
        return null;
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
    public boolean chunkExists(int p_217354_1_, int p_217354_2_) {
        return false;
    }

    @Override
    public void playEvent(@Nullable PlayerEntity player, int type, BlockPos pos, int data) {

    }

    @Override
    public ITickList<Block> getPendingBlockTicks() {
        return null;
    }

    @Override
    public ITickList<Fluid> getPendingFluidTicks() {
        return null;
    }
}
