package jeresources.handler;

import jeresources.api.restrictions.BlockRestriction;
import jeresources.api.restrictions.DimensionRegistry;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
