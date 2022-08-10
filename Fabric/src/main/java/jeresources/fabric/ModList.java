package jeresources.fabric;

import jeresources.platform.IModInfo;
import jeresources.platform.IModList;
import net.fabricmc.loader.api.FabricLoader;

import java.util.List;

public class ModList implements IModList {
    @Override
    public List<? extends IModInfo> getMods() {
        return FabricLoader.getInstance().getAllMods().stream().map(ModInfo::new).toList();
    }

    @Override
    public boolean isLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }
}
