package jeresources.api;

import jeresources.api.conditionals.LightLevel;
import jeresources.api.render.IMobRenderHook;
import jeresources.api.render.IScissorHook;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

/**
 * Use to register new {@link EntityLivingBase}s, {@link IMobRenderHook}s and {@link IScissorHook}s
 */
public interface IMobRegistry
{
    /**
     * Register a custom {@link EntityLivingBase} with given parameters
     * Implement {@link jeresources.api.conditionals.ICustomEntityProperty} and {@link jeresources.api.conditionals.ICustomLootFunction}
     * to gain more control over the information added to the tooltips when using custom
     * {@link net.minecraft.world.storage.loot.properties.EntityProperty}s and {@link net.minecraft.world.storage.loot.functions.LootFunction}s
     *
     * @param entity the {@link EntityLivingBase} instance
     * @param lightLevel the {@link LightLevel} the {@link EntityLivingBase} spawns at
     * @param minExp minimum exp gained by killing the {@link EntityLivingBase}
     * @param maxExp maximum exp gained by killing the {@link EntityLivingBase}
     * @param biomes {@link java.util.List} of {@link String} names of the biomes
     * @param lootTable the {@link ResourceLocation} of the loot table
     */
    void register(EntityLivingBase entity, LightLevel lightLevel, int minExp, int maxExp, String[] biomes, ResourceLocation lootTable);
    void register(EntityLivingBase entity, LightLevel lightLevel, int minExp, int maxExp,  ResourceLocation lootTable);
    void register(EntityLivingBase entity, LightLevel lightLevel, int exp, String[] biomes,  ResourceLocation lootTable);
    void register(EntityLivingBase entity, LightLevel lightLevel, int exp, ResourceLocation lootTable);
    void register(EntityLivingBase entity, LightLevel lightLevel, String[] biomes,  ResourceLocation lootTable);
    void register(EntityLivingBase entity, LightLevel lightLevel, ResourceLocation lootTable);
    void register(EntityLivingBase entity, ResourceLocation lootTable);

    /**
     * Add a hook for scissoring in the mob view
     * The stacktrace will be used to see what called the render
     *
     * @param caller the class that will call the render
     * @param scissorHook your {@link IScissorHook}
     */
    void registerScissorHook(Class caller, IScissorHook scissorHook);

    /**
     * Add a {@link IMobRenderHook} for the given mob type
     * The render hook will be called when rendering in the mob view of JER
     *
     * @param entity the  {@link Class} of the {@link EntityLivingBase}
     * @param renderHook the {@link IMobRenderHook} to be applied
     */
    void registerRenderHook(Class<? extends EntityLivingBase> entity, IMobRenderHook renderHook);
}
