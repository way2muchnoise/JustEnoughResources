package jeresources.util;

import jeresources.api.restrictions.BlockRestriction;
import jeresources.api.restrictions.DimensionRegistry;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldEventHelper
{
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        if (!DimensionRegistry.contains(event.world.provider.getDimension()))
        {
            DimensionRegistry.registerDimension(BlockRestriction.STONE, event.world.provider.getDimension(), false/*ModList.mystcraft.isLoaded() && MystCompat.isMystDim(event.world.provider.dimensionId)*/);
        }
    }
}
