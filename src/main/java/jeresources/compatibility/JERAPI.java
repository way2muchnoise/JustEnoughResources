package jeresources.compatibility;

import jeresources.api.*;
import jeresources.util.ReflectionHelper;
import net.minecraft.world.World;

public class JERAPI implements IJERAPI {
    private IWorldGenRegistry worldGenRegistry;
    private IMobRegistry mobRegistry;
    private IPlantRegistry plantRegistry;
    private IDungeonRegistry dungeonRegistry;
    private static IJERAPI instance;

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

    public static void init() {
        ReflectionHelper.injectIntoFields(JERPlugin.class, IJERAPI.class, JERAPI.getInstance());
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
    public World getWorld() {
        return CompatBase.getWorld();
    }
}
