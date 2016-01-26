package jeresources.compatibility;

import jeresources.api.*;
import jeresources.utils.ReflectionHelper;
import net.minecraftforge.fml.common.discovery.ASMDataTable;

public class JERAPI implements IJERAPI
{
    private IWorldGenRegistry worldGenRegistry;
    private IMobRegistry mobRegistry;
    private static IJERAPI instance;

    public static IJERAPI getInstance()
    {
        if (instance == null)
            instance = new JERAPI();
        return instance;
    }

    private JERAPI()
    {
        worldGenRegistry = new WorldGenRegistryImpl();
        mobRegistry = new MobRegistryImpl();
    }

    public static void init(ASMDataTable asmDataTable)
    {
        ReflectionHelper.injectIntoFields(asmDataTable, JERPlugin.class, IJERAPI.class, JERAPI.getInstance());
    }

    @Override
    public IMobRegistry getMobRegistry()
    {
        return mobRegistry;
    }

    @Override
    public IWorldGenRegistry getWorldGenRegistry()
    {
        return worldGenRegistry;
    }
}
