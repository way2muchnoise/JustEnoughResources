package jeresources.api;

import net.minecraft.world.World;

/**
 * Will be delivered during {@link net.minecraftforge.fml.common.event.FMLPreInitializationEvent}
 * to any public static field that is annotated with {@link JERPlugin}
 */
public interface IJERAPI
{
    IMobRegistry getMobRegistry();
    IWorldGenRegistry getWorldGenRegistry();
    IPlantRegistry getPlantRegistry();
    IDungeonRegistry getDungeonRegistry();

    /**
     * Don't call this every time you need it,
     * as when there is no actual mc world a fake world will be created.
     * Thus uses time and memory
     *
     * @return The current {@link World} used by JER
     */
    World getWorld();
}
