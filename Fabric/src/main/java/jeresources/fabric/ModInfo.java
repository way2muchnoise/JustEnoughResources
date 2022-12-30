package jeresources.fabric;

import jeresources.platform.IModInfo;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackResources;

import java.nio.file.Path;
import java.util.List;

public class ModInfo implements IModInfo {

    private ModContainer modContainer;

    public ModInfo(ModContainer modContainer) {
        this.modContainer = modContainer;
    }

    @Override
    public String getName() {
        return this.modContainer.getOrigin().getParentModId();
    }

    @Override
    public List<? extends PackResources> getPackResources() {
        return this.modContainer.getRootPaths().stream().map(Path::toFile).map(FilePackResources::new).toList();
    }
}
