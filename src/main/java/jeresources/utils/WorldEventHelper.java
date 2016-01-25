package jeresources.utils;

import jeresources.api.restrictions.BlockRestriction;
import jeresources.api.restrictions.DimensionRegistry;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class WorldEventHelper
{
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        if (!DimensionRegistry.contains(event.world.provider.getDimensionId()))
        {
            DimensionRegistry.registerDimension(BlockRestriction.STONE, event.world.provider.getDimensionId(), false/*ModList.mystcraft.isLoaded() && MystCompat.isMystDim(event.world.provider.dimensionId)*/);
        }
    }
}
