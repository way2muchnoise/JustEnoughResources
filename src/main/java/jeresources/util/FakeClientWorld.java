package jeresources.util;

import com.mojang.datafixers.DataFixer;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.tags.NetworkTagManager;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.*;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.dimension.OverworldDimension;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.template.TemplateManager;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.SessionLockException;
import net.minecraft.world.storage.WorldInfo;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class FakeClientWorld extends World {
    public static final WorldSettings worldSettings = new WorldSettings(0, GameType.SURVIVAL, true, false, WorldType.DEFAULT);
    public static final WorldInfo worldInfo = new WorldInfo(worldSettings, "just_enough_resources_fake");
    public static final FakeSaveHandler saveHandler = new FakeSaveHandler();
    public static final Dimension worldProvider = new OverworldDimension() {
        @Override
        public long getWorldTime() {
            return worldInfo.getGameTime();
        }
    };

    private CapabilityDispatcher capabilities;

    public FakeClientWorld() {
        super(saveHandler, null, worldInfo, worldProvider, new Profiler(), true);
        this.capabilities = ForgeEventFactory.gatherCapabilities(FakeClientWorld.class, this);
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capabilities == null ? null : capabilities.getCapability(capability, facing);
    }


    @Override
    protected IChunkProvider createChunkProvider() {
        return new IChunkProvider() {
            @Nullable
            @Override
            public Chunk provideChunk(int i, int i1, boolean b, boolean b1) {
                return null;
            }

            @Override
            public boolean tick(BooleanSupplier booleanSupplier) {
                return false;
            }

            @Override
            public String makeString() {
                return "";
            }

            @Override
            public IChunkGenerator<?> getChunkGenerator() {
                return null;
            }

        };
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
    public NetworkTagManager getTags() {
        return null;
    }

    @Override
    public boolean isChunkLoaded(int x, int z, boolean allowEmpty) {
        return false;
    }

    @Override
    public ITickList<Block> getPendingBlockTicks() {
        return null;
    }

    @Override
    public ITickList<Fluid> getPendingFluidTicks() {
        return null;
    }

    private static class FakeSaveHandler implements ISaveHandler {

        @Override
        public WorldInfo loadWorldInfo() {
            return worldInfo;
        }

        @Override
        public void checkSessionLock() {

        }

        @Override
        public IChunkLoader getChunkLoader(Dimension dimension) {
            return new IChunkLoader() {
                @Nullable
                @Override
                public Chunk loadChunk(IWorld world, int i, int i1, Consumer<Chunk> consumer) throws IOException {
                    return null;
                }

                @Nullable
                @Override
                public ChunkPrimer loadChunkPrimer(IWorld world, int i, int i1, Consumer<IChunk> consumer) throws IOException {
                    return null;
                }

                @Override
                public void saveChunk(World world, IChunk chunk) throws IOException, SessionLockException {

                }

                @Override
                public void flush() {

                }
            };
        }

        @Override
        public void saveWorldInfoWithPlayer(WorldInfo worldInformation, NBTTagCompound tagCompound) {

        }

        @Override
        public void saveWorldInfo(WorldInfo worldInformation) {

        }

        @Override
        public IPlayerFileData getPlayerNBTManager() {
            return new IPlayerFileData() {
                @Override
                public void writePlayerData(EntityPlayer player) {

                }

                @Override
                public NBTTagCompound readPlayerData(EntityPlayer player) {
                    return new NBTTagCompound();
                }

                @Override
                public String[] getAvailablePlayerDat() {
                    return new String[0];
                }
            };
        }

        @Override
        public void flush() {

        }

        @Override
        public File getWorldDirectory() {
            return null;
        }

        @Nullable
        @Override
        public File func_212423_a(DimensionType dimensionType, String s) {
            return null;
        }

        @Override
        public TemplateManager getStructureTemplateManager() {
            return null;
        }

        @Override
        public DataFixer getFixer() {
            return null;
        }
    }
}
