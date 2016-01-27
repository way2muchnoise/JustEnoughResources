package jeresources.compatibility;

import jeresources.config.Settings;
import jeresources.json.WorldGenAdapter;

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
        for (ModList mod : ModList.values())
            mod.initialise(initWorldGen);
        MobRegistryImpl.commit();
        PlantRegistryImpl.commit();
        if (initWorldGen)
            WorldGenRegistryImpl.commit();
        Settings.initedCompat = true;
    }
}
