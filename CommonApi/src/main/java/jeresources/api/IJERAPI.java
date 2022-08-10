package jeresources.api;


import net.minecraft.world.level.Level;

/**
 * Will be delivered during {@link net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent}
 * to any public static field that is annotated with {@link JERPlugin}
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
