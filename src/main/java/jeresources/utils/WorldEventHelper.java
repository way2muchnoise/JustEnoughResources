package jeresources.utils;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import jeresources.api.utils.restrictions.BlockRestriction;
import jeresources.api.utils.restrictions.DimensionRegistry;
import jeresources.compatibility.mystcraft.MystCompat;
import net.minecraftforge.event.world.WorldEvent;

public class WorldEventHelper
{
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event)
    {
        if (!DimensionRegistry.contains(event.world.provider.dimensionId))
        {
            DimensionRegistry.registerDimension(BlockRestriction.STONE, event.world.provider.dimensionId, ModList.mystcraft.isLoaded() && MystCompat.isMystDim(event.world.provider.dimensionId));
        }
    }
}
