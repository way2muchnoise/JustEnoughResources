package jeresources.compatibility;

import jeresources.compatibility.api.JERAPI;
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
            LogHelper.warn("Error during loading of DIY data", e);
        }

        try {
            new MinecraftCompat().init(initWorldGen);
        } catch (Exception e) {
            LogHelper.warn("Error during loading of default minecraft compat", e);
        }

        // Protection is implemented at VillagerEntry creation
        VillagersHelper.initRegistry(VillagerRegistry.getInstance());

        JERAPI.commit(initWorldGen);
    }
}
