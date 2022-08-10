package jeresources.platform;

import java.util.List;

public interface IModList {
    List<? extends IModInfo> getMods();

    boolean isLoaded(String modId);
}
