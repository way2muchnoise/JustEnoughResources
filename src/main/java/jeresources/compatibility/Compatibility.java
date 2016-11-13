package jeresources.compatibility;

import jeresources.compatibility.minecraft.MinecraftCompat;
import jeresources.config.Settings;
import jeresources.json.WorldGenAdapter;
import jeresources.registry.VillagerRegistry;
import jeresources.util.VillagersHelper;

public class Compatibility
{
    public static void init()
    {
        boolean initWorldGen = true;
        if (Settings.useDIYdata)
        {
            if (WorldGenAdapter.hasWorldGenDIYData())
            {
                WorldGenAdapter.readDIYData();
                initWorldGen = false;
            }
        }
        new MinecraftCompat().init(initWorldGen);
        DungeonRegistryImpl.commit();
        MobRegistryImpl.commit();
        PlantRegistryImpl.commit();
        VillagersHelper.initRegistry(VillagerRegistry.getInstance());
        if (initWorldGen)
            WorldGenRegistryImpl.commit();
    }
}
