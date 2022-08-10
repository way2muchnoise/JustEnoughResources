package jeresources.forge;

import jeresources.platform.IModInfo;
import jeresources.platform.IModList;

import java.util.List;

public class ModList implements IModList {

    private net.minecraftforge.fml.ModList modList;

    public ModList(net.minecraftforge.fml.ModList modList) {
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
