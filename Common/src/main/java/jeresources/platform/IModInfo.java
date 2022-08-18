package jeresources.platform;

import net.minecraft.server.packs.PackResources;

public interface IModInfo {
    String getName();

    PackResources getPackResources();
}
