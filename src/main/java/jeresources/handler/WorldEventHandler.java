package jeresources.handler;

import jeresources.api.restrictions.BlockRestriction;
import jeresources.api.restrictions.DimensionRegistry;
import jeresources.compatibility.CompatBase;
import jeresources.compatibility.Compatibility;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.world.World;
import net.minecraft.world.demo.DemoWorldServer;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class WorldEventHandler
{
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        int dimId = event.getWorld().provider.getDimensionType().getId();
        if (!DimensionRegistry.contains(dimId))
        {
            DimensionRegistry.registerDimension(BlockRestriction.STONE, dimId, false/*ModList.mystcraft.isLoaded() && MystCompat.isMystDim(event.world.provider.dimensionId)*/);
        }
    }
}
