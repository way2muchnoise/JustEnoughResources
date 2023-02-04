package jeresources.fabric;

import jeresources.platform.IModInfo;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PathPackResources;

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
        return this.modContainer.getContainedMods().stream().map(ModInfo::getPackResources).flatMap(List::stream).toList();
    }

    private static List<? extends PackResources> getPackResources(ModContainer modContainer) {
        return modContainer.getRootPaths().stream().map(path -> new PathPackResources(modContainer.getMetadata().getId(), path, false)).toList();
    }
}
