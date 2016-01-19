package jeresources.compatibility;

import jeresources.config.Settings;
import jeresources.json.OreAdapter;
import jeresources.registry.MessageRegistry;
import jeresources.utils.ModList;

public class Compatibility
{
    public static final float DENSE_ORES_MULTIPLIER = 1F + 2F * (1000F / 20480F);

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

        /*if (ModList.denseores.isLoaded())
        {
            for (String oreDictEntry : OreDictionary.getOreNames())
            {
                if (oreDictEntry!= null && oreDictEntry.startsWith("denseore") && OreDictionary.getOres(oreDictEntry).size() > 0)
                {
                    ItemStack denseOre = OreDictionary.getOres(oreDictEntry).get(0);
                    ItemStack ore = OreDictionary.getOres(oreDictEntry.replace("dense", "")).get(0);
                    OreRegistry.addDrops(new ModifyOreMessage(ore, denseOre));
                }
            }
        }*/

        MessageRegistry.processMessages(initOres);
        Settings.initedCompat = true;
    }
}
