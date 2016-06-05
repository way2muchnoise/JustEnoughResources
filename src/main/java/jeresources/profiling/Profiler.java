package jeresources.profiling;

import jeresources.jei.JEIConfig;
import jeresources.json.ProfilingAdapter;
import mezz.jei.api.IJeiHelpers;
import net.minecraft.command.ICommandSender;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Profiler implements Runnable
{
    private final ConcurrentMap<Integer, ProfiledDimensionData> allDimensionData;
    private final ProfilingTimer timer;
    private final ICommandSender sender;
    private final int chunkCount;
    private final boolean allWorlds;
    private ProfilingExecutor currentExecutor;

    private Profiler(ICommandSender sender, int chunkCount, boolean allWorlds)
    {
        this.sender = sender;
        this.allDimensionData = new ConcurrentHashMap<>();
        this.chunkCount = chunkCount;
        this.timer = new ProfilingTimer(sender, chunkCount);
        this.allWorlds = allWorlds;
    }

    @Override
    public void run()
    {
        if (!allWorlds)
        {
            WorldServer world = (WorldServer) this.sender.getEntityWorld();
            profileWorld(world);
        } else
        {
            for (WorldServer world : DimensionManager.getWorlds())
                profileWorld(world);
        }

        writeData();

        this.timer.complete();

        IJeiHelpers jeiHelpers = JEIConfig.getJeiHelpers();
        if (jeiHelpers != null)
        {
            jeiHelpers.reload();
        }
    }

    private void profileWorld(final WorldServer worldServer)
    {
        final ProfilingExecutor executor = new ProfilingExecutor(this);
        this.currentExecutor = executor;
        int dimId = worldServer.provider.getDimensionType().getId();
        this.allDimensionData.put(dimId, new ProfiledDimensionData());

        DummyWorld dummyWorld = new DummyWorld(worldServer);
        dummyWorld.init();
        ChunkGetter chunkGetter = new ChunkGetter(chunkCount, dummyWorld, executor);
        worldServer.addScheduledTask(chunkGetter);

        executor.awaitTermination();
        this.currentExecutor = null;

        dummyWorld.clearChunks();
    }

    public ProfilingTimer getTimer() {
        return timer;
    }

    public ConcurrentMap<Integer, ProfiledDimensionData> getAllDimensionData() {
        return allDimensionData;
    }

    private void writeData()
    {
        Map<Integer, ProfilingAdapter.DimensionData> allData = new HashMap<>();
        for (Integer dim : this.allDimensionData.keySet())
        {
            ProfiledDimensionData profiledData = this.allDimensionData.get(dim);
            ProfilingAdapter.DimensionData data = new ProfilingAdapter.DimensionData();
            data.dropsMap = profiledData.dropsMap;
            data.silkTouchMap = profiledData.silkTouchMap;

            for (Map.Entry<String, Integer[]> entry : profiledData.distributionMap.entrySet())
            {
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

    public static boolean init(ICommandSender sender, int chunks, boolean allWorlds)
    {
        if (currentProfiler != null && !currentProfiler.timer.isCompleted()) return false;
        currentProfiler = new Profiler(sender, chunks, allWorlds);
        new Thread(currentProfiler).start();
        return true;
    }

    public static boolean stop()
    {
        if (currentProfiler == null || currentProfiler.timer.isCompleted()) return false;
        if (currentProfiler.currentExecutor != null)
            currentProfiler.currentExecutor.shutdownNow();
        currentProfiler.writeData();
        return true;
    }

}
