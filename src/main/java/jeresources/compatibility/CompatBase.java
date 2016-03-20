package jeresources.compatibility;

import jeresources.api.render.IMobRenderHook;
import jeresources.entry.MobEntry;
import jeresources.entry.PlantEntry;
import jeresources.entry.WorldGenEntry;
import jeresources.registry.MobRegistry;
import jeresources.registry.PlantRegistry;
import jeresources.registry.WorldGenRegistry;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public abstract class CompatBase
{
    protected static World world = DimensionManager.getWorld(0);

    public abstract void init(boolean worldGen);

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
