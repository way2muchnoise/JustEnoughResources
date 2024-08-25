package jeresources.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;

public class RegistryHelper {
    public static <T> Registry<T> getRegistry(ResourceKey<? extends Registry<T>> key) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level == null) {
            throw new IllegalStateException("Could not get registry, registry access is unavailable because the level is currently null");
        }
        RegistryAccess registryAccess = level.registryAccess();
        return registryAccess.registryOrThrow(key);
    }

    public static <T> Holder<T> getHolder(ResourceKey<? extends Registry<T>> registry, ResourceKey<T> key) {
        return getRegistry(registry).getHolderOrThrow(key);
    }

    public static <T> T getObject(ResourceKey<? extends Registry<T>> registry, ResourceKey<T> key) {
        return getRegistry(registry).get(key);
    }
}
