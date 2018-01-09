package jeresources.profiling;

import jeresources.config.Settings;
import jeresources.json.ProfilingAdapter;
import jeresources.util.LogHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Profiler implements Runnable {
    private final ConcurrentMap<Integer, ProfiledDimensionData> allDimensionData;
    private final ProfilingTimer timer;
    private final ICommandSender sender;
    private final int chunkCount;
    private final boolean allWorlds;
    private ProfilingExecutor currentExecutor;

    private Profiler(ICommandSender sender, int chunkCount, boolean allWorlds) {
        this.sender = sender;
        this.allDimensionData = new ConcurrentHashMap<>();
        this.chunkCount = chunkCount;
        this.timer = new ProfilingTimer(sender, chunkCount);
        this.allWorlds = allWorlds;
    }

    @Override
    public void run() {
        LogHelper.warn("There will be messages about world gen lag during the profiling, you can ignore these as that is what you get when profiling.");
        if (!allWorlds) {
            WorldServer world = (WorldServer) this.sender.getEntityWorld();
            Entity sendEnt = sender.getCommandSenderEntity();
    		int dimId = sendEnt.dimension;
    		if(world == null) {
				DimensionManager.initDimension(dimId);
				world = DimensionManager.getWorld(dimId);
    		}				
    		profileWorld(world, dimId);
        } else {		
        	Integer[] dimIds = DimensionManager.getStaticDimensionIDs();
        	for (int i = 0; i < dimIds.length; i++) {
        		int dimId = dimIds[i];
        		WorldServer world = DimensionManager.getWorld(dimId);
        		if(world == null) {
					DimensionManager.initDimension(dimId);
					world = DimensionManager.getWorld(dimId);
        		}				
        		profileWorld(world, dimId);
        	}
        }

        writeData();

        this.timer.complete();
    }

    private void profileWorld(final WorldServer worldServer, int dimId) {
    	String msg2 = "Inspecting dimension ";
    	try { 
    			msg2 += worldServer.provider.getDimensionType().getName() + ": "; 
    	}
    	catch (Exception x)
    	{
    		msg2 += " 'Dim Name Error' ";
    	}
    	msg2 += dimId + ".";
        LogHelper.info(msg2);
        sender.sendMessage(new TextComponentString(msg2));

        if (Settings.excludedDimensions.contains(dimId)) {
            String msg = "Skipped dimension " + dimId + " during profiling";
            LogHelper.info(msg);
            sender.sendMessage(new TextComponentString(msg));
            return;
        }

        final ProfilingExecutor executor = new ProfilingExecutor(this);
        this.currentExecutor = executor;
        this.allDimensionData.put(dimId, new ProfiledDimensionData());

        DummyWorld dummyWorld = new DummyWorld(worldServer, dimId);
        dummyWorld.init();
        ChunkGetter chunkGetter = new ChunkGetter(chunkCount, dummyWorld, executor, dimId);
        worldServer.addScheduledTask(chunkGetter);

        executor.awaitTermination();
        this.currentExecutor = null;

        dummyWorld.clearChunks();
        // Return the world to it's original state
        DimensionManager.setWorld(dimId, worldServer, Minecraft.getMinecraft().getIntegratedServer());
    }

    public ProfilingTimer getTimer() {
        return timer;
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

    public static boolean init(ICommandSender sender, int chunks, boolean allWorlds) {
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
