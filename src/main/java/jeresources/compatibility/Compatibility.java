package jeresources.compatibility;

import jeresources.config.Settings;
import jeresources.json.OreAdapter;
import jeresources.registry.MessageRegistry;
import jeresources.utils.ModList;

public class Compatibility
{
    public static void init()
    {
        boolean initOres = true;
        if (Settings.useDIYdata)
        {
            if (OreAdapter.hasOreEntry())
            {
                OreAdapter.readEntrys();
                initOres = false;
            }
        }
        for (ModList mod : ModList.values())
            mod.initialise(initOres);

        MessageRegistry.processMessages(initOres);
        Settings.initedCompat = true;
    }
}
