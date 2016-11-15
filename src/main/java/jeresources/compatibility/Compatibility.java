package jeresources.compatibility;

import jeresources.compatibility.minecraft.MinecraftCompat;
import jeresources.config.Settings;
import jeresources.json.WorldGenAdapter;
import jeresources.registry.VillagerRegistry;
import jeresources.util.LogHelper;
import jeresources.util.VillagersHelper;

public class Compatibility {
    public static void init() {
        boolean initWorldGen = true;

        try {
            if (Settings.useDIYdata) {
                if (WorldGenAdapter.hasWorldGenDIYData()) {
                    WorldGenAdapter.readDIYData();
                    initWorldGen = false;
                }
            }
        } catch (Exception e) {
            LogHelper.warn("Error during loading of DIY data");
        }

        try {
            new MinecraftCompat().init(initWorldGen);
        } catch (Exception e) {
            LogHelper.warn("Error during loading of default minecraft compat");
        }

        // Protection is implemented at VillagerEntry creation
        VillagersHelper.initRegistry(VillagerRegistry.getInstance());

        // API implements there own abuse protection
        DungeonRegistryImpl.commit();
        MobRegistryImpl.commit();
        PlantRegistryImpl.commit();
        if (initWorldGen)
            WorldGenRegistryImpl.commit();
    }
}
