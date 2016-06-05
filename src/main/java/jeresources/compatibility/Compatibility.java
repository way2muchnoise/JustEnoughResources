package jeresources.compatibility;

import jeresources.compatibility.minecraft.MinecraftCompat;
import jeresources.config.Settings;
import jeresources.json.WorldGenAdapter;
import net.minecraft.client.Minecraft;

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
        MobRegistryImpl.commit();
        PlantRegistryImpl.commit();
        if (initWorldGen)
            WorldGenRegistryImpl.commit();
        VillagerRegistryImpl.commit();
    }
}
