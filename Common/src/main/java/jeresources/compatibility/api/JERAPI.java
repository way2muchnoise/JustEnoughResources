package jeresources.compatibility.api;

import jeresources.api.*;
import jeresources.compatibility.CompatBase;
import jeresources.platform.Services;
import net.minecraft.world.level.Level;

public class JERAPI implements IJERAPI {
    private IWorldGenRegistry worldGenRegistry;
    private IMobRegistry mobRegistry;
    private IPlantRegistry plantRegistry;
    private IDungeonRegistry dungeonRegistry;
    private static IJERAPI instance;
    private static boolean pluginsInjected = false;

    public static IJERAPI getInstance() {
        if (instance == null)
            instance = new JERAPI();
        return instance;
    }

    private JERAPI() {
        worldGenRegistry = new WorldGenRegistryImpl();
        mobRegistry = new MobRegistryImpl();
        plantRegistry = new PlantRegistryImpl();
        dungeonRegistry = new DungeonRegistryImpl();
    }

    /**
     * Hands the API to every plugin, once per session.
     *
     * This is called from {@link jeresources.compatibility.Compatibility#init()}
     * rather than from mod setup on purpose. Plugins register {@link net.minecraft.world.item.ItemStack}s,
     * and since Minecraft 26.1 a stack cannot be built before its item's data components
     * have been bound, which only happens once the server resources have loaded.
     * Compatibility runs after that point - it is the same moment JER registers its
     * own vanilla data - so plugins called from here can use the API as documented.
     *
     * Plugin registrations are buffered and re-applied by {@link #commit(boolean)}
     * on every reload, so plugins only ever have to be asked once.
     */
    public static void init() {
        if (pluginsInjected)
            return;
        pluginsInjected = true;
        Services.PLATFORM.injectApi(JERAPI.getInstance());
    }

    @Override
    public IMobRegistry getMobRegistry() {
        return mobRegistry;
    }

    @Override
    public IWorldGenRegistry getWorldGenRegistry() {
        return worldGenRegistry;
    }

    @Override
    public IPlantRegistry getPlantRegistry() {
        return plantRegistry;
    }

    @Override
    public IDungeonRegistry getDungeonRegistry() {
        return dungeonRegistry;
    }

    @Override
    public Level getLevel() {
        return CompatBase.getLevel();
    }

    public static void commit(boolean initWorldGen) {
        // API implements their own abuse protection
        DungeonRegistryImpl.commit();
        MobRegistryImpl.commit();
        PlantRegistryImpl.commit();
        if (initWorldGen)
            WorldGenRegistryImpl.commit();
    }
}
