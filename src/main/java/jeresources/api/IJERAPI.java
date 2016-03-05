package jeresources.api;

/**
 * Will be delivered during {@link net.minecraftforge.fml.common.event.FMLPreInitializationEvent}
 * to any public static field that is annotated with {@link JERPlugin}
 */
public interface IJERAPI
{
    IMobRegistry getMobRegistry();
    IWorldGenRegistry getWorldGenRegistry();
    IPlantRegistry getPlantRegistry();
    IVillagerRegistry getVillagerRegistry();
}
