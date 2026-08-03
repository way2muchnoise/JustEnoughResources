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
        try {
            JERAPI.init();
        } catch (Exception e) {
            LogHelper.warn("Error during loading of API plugins", e);
        }

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

        try {
            VillagersHelper.initRegistry(VillagerRegistry.getInstance());
        } catch (Exception e) {
            LogHelper.warn("Error during loading of villager trades", e);
        }

        JERAPI.commit(initWorldGen);
    }
}
