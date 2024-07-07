package jeresources.neoforge;

import jeresources.platform.IModInfo;
import net.minecraft.server.packs.PackResources;
import net.neoforged.neoforge.resource.ResourcePackLoader;
import net.neoforged.neoforgespi.language.IModFileInfo;

import java.util.List;

public class ModInfo implements IModInfo {

    private IModFileInfo modFile;

    public ModInfo(IModFileInfo modFile) {
        this.modFile = modFile;
    }

    @Override
    public String getName() {
        return modFile.moduleName();
    }

    @Override
    public List<? extends PackResources> getPackResources() {
        // return List.of(ResourcePackLoader.createPackForMod(modFile).openPrimary(modFile.moduleName()));
        return List.of(ResourcePackLoader.createPackForMod(modFile).openPrimary(null)); // TODO Fix if needed
    }
}
