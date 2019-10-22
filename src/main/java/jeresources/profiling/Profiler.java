package jeresources.profiling;

import jeresources.config.Settings;
import jeresources.json.ProfilingAdapter;
import jeresources.util.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.DimensionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Profiler implements Runnable {
    private final ConcurrentMap<Integer, ProfiledDimensionData> allDimensionData;
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
            DimensionType dimensionType = sender.dimension;
            profileWorld(dimensionType);
            
        } else {        

            //getRegistry gets ALL of the dimensions.
            //Forge says it is internal, but there are not other options for
            //all dimensions that exist.
            for (Object o : DimensionManager.getRegistry()) {
                profileWorld((DimensionType) o);
            }
        }

        writeData();

        this.timer.complete();
    }

    private void profileWorld(DimensionType dimensionType) {
        String msg;
        MinecraftServer server = Minecraft.getInstance().getIntegratedServer();
        //Get the world we want to process.
        ServerWorld world = DimensionManager.getWorld(server, dimensionType, true, true);

        if (world == null) {
            msg = "Unable to profile dimension " + dimensionType + ".  There is no world for it.";
            LogHelper.error(msg);
            sender.sendMessage(new StringTextComponent(msg));
            return;
        }
        
        //Make this stick for recovery after profiling.
        final ServerWorld worldServer = world;
        
        msg = "Inspecting dimension " + dimensionType + ": " + worldServer.getDimension().getType().getRegistryName() + ". ";
        sender.sendMessage(new StringTextComponent(msg));
        
        msg += "The world thinks it is dimension " + worldServer.getDimension().getType().getRegistryName() + ".";
        LogHelper.info(msg);

        
        if (Settings.excludedDimensions.contains(dimensionType.getId())) {
            msg = "Skipped dimension " + dimensionType + " during profiling";
            LogHelper.info(msg);
            sender.sendMessage(new StringTextComponent(msg));
            return;
        }

        final ProfilingExecutor executor = new ProfilingExecutor(this);
        this.currentExecutor = executor;
        this.allDimensionData.put(dimensionType.getId(), new ProfiledDimensionData());

        DummyWorld dummyWorld = new DummyWorld(worldServer);
        // dummyWorld.initialize(null);
        ChunkGetter chunkGetter = new ChunkGetter(chunkCount, dummyWorld, executor);
        worldServer.getServer().deferTask(chunkGetter);

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

    public ConcurrentMap<Integer, ProfiledDimensionData> getAllDimensionData() {
        return allDimensionData;
    }

    private void writeData() {
        Map<Integer, ProfilingAdapter.DimensionData> allData = new HashMap<>();
        for (Integer dim : this.allDimensionData.keySet()) {
            ProfiledDimensionData profiledData = this.allDimensionData.get(dim);
            ProfilingAdapter.DimensionData data = new ProfilingAdapter.DimensionData();
            data.dropsMap = profiledData.dropsMap;
            data.silkTouchMap = profiledData.silkTouchMap;

            for (Map.Entry<String, Integer[]> entry : profiledData.distributionMap.entrySet()) {
                Float[] array = new Float[ChunkProfiler.CHUNK_HEIGHT];
                for (int i = 0; i < ChunkProfiler.CHUNK_HEIGHT; i++)
                    array[i] = entry.getValue()[i] * 1.0F / this.timer.getBlocksPerLayer(dim);
                data.distribution.put(entry.getKey(), array);
            }

            allData.put(dim, data);
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
