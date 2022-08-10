package jeresources.proxy;

import jeresources.compatibility.Compatibility;
import jeresources.config.Settings;
import jeresources.registry.*;

public class CommonProxy {
    public void initCompatibility() {
        DungeonRegistry.getInstance().clear();
        MobRegistry.getInstance().clear();
        PlantRegistry.getInstance().clear();
        VillagerRegistry.getInstance().clear();
        WorldGenRegistry.getInstance().clear();
        Compatibility.init();
        EnchantmentRegistry.getInstance().removeAll(Settings.excludedEnchants);
    }
}
