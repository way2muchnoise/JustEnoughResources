package jeresources.api;


import net.minecraft.world.level.Level;

/**
 * Will be delivered during startup to any public static field that is:
 * Forge: annotated with {@link JERPlugin}
 * Fabric: mentioned as entrypoint {@link IJERPlugin#entry_point} in fabric.mod.json
 */
public interface IJERAPI {
    IMobRegistry getMobRegistry();
    IWorldGenRegistry getWorldGenRegistry();
    IPlantRegistry getPlantRegistry();
    IDungeonRegistry getDungeonRegistry();

    /**
     * Don't call this every time you need it,
     * as when there is no actual mc world a fake world will be created.
     * Thus uses time and memory
     *
     * @return The current {@link Level} used by JER
     */
    Level getLevel();
}
