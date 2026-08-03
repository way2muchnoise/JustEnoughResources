package jeresources.api;


import net.minecraft.world.level.Level;

/**
 * Delivered to every {@link IJERPlugin} once per session, at the same point JER
 * gathers its own data: after the world's resources have loaded, and before the
 * registries are handed to JEI.
 *
 * That timing is part of the contract. {@link net.minecraft.world.item.ItemStack}s
 * cannot be built during mod setup on Minecraft 26.1 and later, because an item's
 * data components are not bound until its resources load, so registering from
 * {@link IJERPlugin#receive(IJERAPI)} is both safe and the only supported moment.
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
