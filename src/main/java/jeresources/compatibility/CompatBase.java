package jeresources.compatibility;

import jeresources.api.render.IMobRenderHook;
import jeresources.entries.MobEntry;
import jeresources.entries.PlantEntry;
import jeresources.entries.WorldGenEntry;
import jeresources.registry.MobRegistry;
import jeresources.registry.PlantRegistry;
import jeresources.registry.WorldGenRegistry;
import jeresources.utils.LogHelper;
import jeresources.utils.ReflectionHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public abstract class CompatBase
{
    protected static World world = DimensionManager.getWorld(0);

    public static boolean load(ModList mod, boolean worldGen)
    {
        if (mod.isLoaded())
        {
            LogHelper.info("Loading compatibility for " + mod.getName());
            CompatBase compat = (CompatBase) ReflectionHelper.initialize(mod.compatClass());
            compat.init(worldGen);
            return true;
        } else
        {
            LogHelper.info(mod.getName() + " not loaded - skipping");
        }
        return false;
    }

    protected abstract void init(boolean worldGen);

    public void registerMob(MobEntry entry)
    {
        MobRegistry.getInstance().registerMob(entry);
    }

    public void registerWorldGen(WorldGenEntry entry)
    {
        WorldGenRegistry.getInstance().registerEntry(entry);
    }

    public void registerPlant(PlantEntry entry)
    {
        PlantRegistry.getInstance().registerPlant(entry);
    }

    public void registerMobRenderHook(Class<? extends EntityLivingBase> clazz, IMobRenderHook renderHook)
    {
        JERAPI.getInstance().getMobRegistry().registerRenderHook(clazz, renderHook);
    }
}
