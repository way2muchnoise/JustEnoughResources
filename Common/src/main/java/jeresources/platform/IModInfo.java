package jeresources.platform;

import net.minecraft.server.packs.PackResources;

import java.io.File;

public interface IModInfo {
    String getName();

    PackResources getPackResources();
}
