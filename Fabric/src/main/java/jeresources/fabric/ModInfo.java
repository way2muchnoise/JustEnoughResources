package jeresources.fabric;

import jeresources.platform.IModInfo;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.PackResources;

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
    public PackResources getPackResources() {
        return new FilePackResources(this.modContainer.getRoot().toFile());
    }
}
