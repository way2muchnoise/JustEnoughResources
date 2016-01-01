package jeresources.compatibility;

import jeresources.api.messages.ModifyOreMessage;
import jeresources.config.Settings;
import jeresources.registry.MessageRegistry;
import jeresources.registry.OreRegistry;
import jeresources.utils.ModList;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class Compatibility
{
    public static final float DENSE_ORES_MULTIPLIER = 1F + 2F * (1000F / 20480F);

    public static void init()
    {
        for (ModList mod : ModList.values())
        {
            mod.initialise();
        }

        if (ModList.denseores.isLoaded())
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
        }

        MessageRegistry.processMessages();
        Settings.initedCompat = true;
    }
}
