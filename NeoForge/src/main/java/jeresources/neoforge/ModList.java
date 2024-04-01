package jeresources.neoforge;

import jeresources.platform.IModInfo;
import jeresources.platform.IModList;

import java.util.List;

public class ModList implements IModList {

    private net.neoforged.fml.ModList modList;

    public ModList(net.neoforged.fml.ModList modList) {
        this.modList = modList;
    }

    @Override
    public List<? extends IModInfo> getMods() {
        return this.modList.getModFiles().stream().map(ModInfo::new).toList();
    }

    @Override
    public boolean isLoaded(String modId) {
        return this.modList.isLoaded(modId);
    }
}
