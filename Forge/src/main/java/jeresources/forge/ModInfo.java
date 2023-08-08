package jeresources.forge;

import jeresources.platform.IModInfo;
import net.minecraft.server.packs.PackResources;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.resource.ResourcePackLoader;

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
        return List.of(ResourcePackLoader.createPackForMod(modFile));
    }
}
