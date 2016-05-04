package jeresources.compatibility;

import jeresources.api.render.IMobRenderHook;
import jeresources.entry.DungeonEntry;
import jeresources.entry.MobEntry;
import jeresources.entry.PlantEntry;
import jeresources.entry.WorldGenEntry;
import jeresources.registry.DungeonRegistry;
import jeresources.registry.MobRegistry;
import jeresources.registry.PlantRegistry;
import jeresources.registry.WorldGenRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public abstract class CompatBase
{
    protected static World getWorld()
    {
        return Minecraft.getMinecraft().theWorld;
    }

    public abstract void init(boolean worldGen);

    protected void registerMob(MobEntry entry)
    {
        MobRegistry.getInstance().registerMob(entry);
    }

    protected void registerDungeonEntry(DungeonEntry entry)
    {
        DungeonRegistry.getInstance().registerDungeonEntry(entry);
    }

    protected void registerWorldGen(WorldGenEntry entry)
    {
        WorldGenRegistry.getInstance().registerEntry(entry);
    }

    protected void registerPlant(PlantEntry entry)
    {
        PlantRegistry.getInstance().registerPlant(entry);
    }

    protected void registerMobRenderHook(Class<? extends EntityLivingBase> clazz, IMobRenderHook renderHook)
    {
        JERAPI.getInstance().getMobRegistry().registerRenderHook(clazz, renderHook);
    }
}
