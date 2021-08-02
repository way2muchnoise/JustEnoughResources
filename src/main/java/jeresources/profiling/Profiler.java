package jeresources.profiling;

import jeresources.config.Settings;
import jeresources.json.ProfilingAdapter;
import jeresources.util.DimensionHelper;
import jeresources.util.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Profiler implements Runnable {
    private final ConcurrentMap<RegistryKey<World>, ProfiledDimensionData> allDimensionData;
    private final ProfilingTimer timer;
    private final Entity sender;
    private final ProfilingBlacklist blacklist;
    private final int chunkCount;
    private final boolean allWorlds;
    private ProfilingExecutor currentExecutor;

    private Profiler(Entity sender, int chunkCount, boolean allWorlds) {
        this.sender = sender;
        this.allDimensionData = new ConcurrentHashMap<>();
        this.chunkCount = chunkCount;
        this.timer = new ProfilingTimer(sender, chunkCount);
        this.allWorlds = allWorlds;
        this.blacklist = new ProfilingBlacklist();
    }

    @Override
    public void run() {
        LogHelper.warn("There will be messages about world gen lag during the profiling, you can ignore these as that is what you get when profiling.");
        if (!allWorlds) {

            // Will never be null as the mod is client side only
            RegistryKey<World> worldKey = sender.level.dimension();
            profileWorld(worldKey);
            
        } else {
            for (RegistryKey<World> worldKey : sender.getServer().levelKeys()) {
                profileWorld(worldKey);
            }
        }

        writeData();

        this.timer.complete();
    }

    private void profileWorld(RegistryKey<World> worldKey) {
        String msg;
        MinecraftServer server = Minecraft.getInstance().getSingleplayerServer();
        //Get the world we want to process.
        ServerWorld world = server.getLevel(worldKey);

        if (world == null) {
            msg = "Unable to profile dimension " + DimensionHelper.getWorldName(worldKey) + ".  There is no world for it.";
            LogHelper.error(msg);
            sender.sendMessage(new StringTextComponent(msg), Util.NIL_UUID);
            return;
        }
        
        //Make this stick for recovery after profiling.
        final ServerWorld worldServer = world;
        
        msg = "Inspecting dimension " + DimensionHelper.getWorldName(worldKey) + ". ";
        sender.sendMessage(new StringTextComponent(msg), Util.NIL_UUID);
        LogHelper.info(msg);

        
        if (Settings.excludedDimensions.contains(worldKey.getRegistryName().toString())) {
            msg = "Skipped dimension " + DimensionHelper.getWorldName(worldKey) + " during profiling";
            LogHelper.info(msg);
            sender.sendMessage(new StringTextComponent(msg), Util.NIL_UUID);
            return;
        }

        final ProfilingExecutor executor = new ProfilingExecutor(this);
        this.currentExecutor = executor;
        this.allDimensionData.put(worldKey, new ProfiledDimensionData());

        DummyWorld dummyWorld = new DummyWorld(worldServer);
        // dummyWorld.initialize(null);
        ChunkGetter chunkGetter = new ChunkGetter(chunkCount, dummyWorld, executor);
        worldServer.getServer().addTickable(chunkGetter);

        executor.awaitTermination();
        this.currentExecutor = null;

        dummyWorld.clearChunks();
        // Return the world to it's original state
        // DimensionManager.setWorld(dimensionType, worldServer, server);
    }

    public ProfilingTimer getTimer() {
        return timer;
    }

    public ProfilingBlacklist getBlacklist() {
        return blacklist;
    }

    public ConcurrentMap<RegistryKey<World>, ProfiledDimensionData> getAllDimensionData() {
        return allDimensionData;
    }

    private void writeData() {
        Map<RegistryKey<World>, ProfilingAdapter.DimensionData> allData = new HashMap<>();
        for (RegistryKey<World> worldRegistryKey : this.allDimensionData.keySet()) {
            ProfiledDimensionData profiledData = this.allDimensionData.get(worldRegistryKey);
            ProfilingAdapter.DimensionData data = new ProfilingAdapter.DimensionData();
            data.dropsMap = profiledData.dropsMap;
            data.silkTouchMap = profiledData.silkTouchMap;

            for (Map.Entry<String, Integer[]> entry : profiledData.distributionMap.entrySet()) {
                Float[] array = new Float[ChunkProfiler.CHUNK_HEIGHT];
                for (int i = 0; i < ChunkProfiler.CHUNK_HEIGHT; i++)
                    array[i] = entry.getValue()[i] * 1.0F / this.timer.getBlocksPerLayer(worldRegistryKey);
                data.distribution.put(entry.getKey(), array);
            }

            allData.put(worldRegistryKey, data);
        }

        ProfilingAdapter.write(allData);
    }

    private static Profiler currentProfiler;

    public static boolean init(Entity sender, int chunks, boolean allWorlds) {
        if (currentProfiler != null && !currentProfiler.timer.isCompleted()) return false;
        currentProfiler = new Profiler(sender, chunks, allWorlds);
        new Thread(currentProfiler).start();
        return true;
    }

    public static boolean stop() {
        if (currentProfiler == null || currentProfiler.timer.isCompleted()) return false;
        if (currentProfiler.currentExecutor != null)
            currentProfiler.currentExecutor.shutdownNow();
        currentProfiler.writeData();
        return true;
    }

}
