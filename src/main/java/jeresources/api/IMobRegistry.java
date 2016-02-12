package jeresources.api;

import jeresources.api.conditionals.LightLevel;
import jeresources.api.conditionals.WatchableData;
import jeresources.api.drop.DropItem;
import jeresources.api.render.IMobRenderHook;
import jeresources.api.render.IScissorHook;
import net.minecraft.entity.EntityLivingBase;

/**
 * Use to register mobs, mob drops, {@link IMobRenderHook}s and {@link IScissorHook}s
 */
public interface IMobRegistry
{
    void register(EntityLivingBase entity, LightLevel lightLevel, int minExp, int maxExp, String[] biomes, DropItem... drops);
    void register(EntityLivingBase entity, LightLevel lightLevel, String[] biomes, DropItem... drops);
    void register(EntityLivingBase entity, LightLevel lightLevel, int exp, String[] biomes, DropItem... drops);
    void register(EntityLivingBase entity, LightLevel lightLevel, int exp, DropItem... drops);
    void register(EntityLivingBase entity, LightLevel lightLevel, int minExp, int maxExp, DropItem... drops);

    void registerDrops(Class<? extends EntityLivingBase> entity, WatchableData watchableData, DropItem... drops);

    /**
     * Add a hook for scissoring in the mob view
     * The stacktrace will be used to see what called the render
     *
     * @param caller the class that will call the render
     * @param scissorHook your {@link IScissorHook}
     */
    void registerScissorHook(Class caller, IScissorHook scissorHook);
    void registerRenderHook(Class<? extends EntityLivingBase> entity, IMobRenderHook renderHook);
}
