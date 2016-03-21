package jeresources.api;

import jeresources.api.conditionals.LightLevel;
import jeresources.api.conditionals.WatchableData;
import jeresources.api.drop.LootDrop;
import jeresources.api.render.IMobRenderHook;
import jeresources.api.render.IScissorHook;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.storage.loot.LootTable;

/**
 * Use to register mobs, mob drops, {@link IMobRenderHook}s and {@link IScissorHook}s
 */
public interface IMobRegistry
{
    void register(EntityLivingBase entityLivingBase, LootTable lootTable);
    void register(EntityLivingBase entity, LightLevel lightLevel, int minExp, int maxExp, String[] biomes, LootDrop... drops);
    void register(EntityLivingBase entity, LightLevel lightLevel, String[] biomes, LootDrop... drops);
    void register(EntityLivingBase entity, LightLevel lightLevel, int exp, String[] biomes, LootDrop... drops);
    void register(EntityLivingBase entity, LightLevel lightLevel, int exp, LootDrop... drops);
    void register(EntityLivingBase entity, LightLevel lightLevel, int minExp, int maxExp, LootDrop... drops);

    /**
     * Adds drops to mobs use {@link WatchableData} to add some more specifics to define the mob
     *
     * @param entity entity {@link Class} extending {@link EntityLivingBase}
     * @param watchableData the {@link WatchableData} to define the extra terms
     * @param drops drops to be added
     */
    void registerDrops(Class<? extends EntityLivingBase> entity, WatchableData watchableData, LootDrop... drops);
    void registerDrops(Class<? extends EntityLivingBase> entity, WatchableData watchableData, LootTable lootTable);
    void registerDrops(Class<? extends EntityLivingBase> entity, LootDrop... drops);
    void registerDrops(Class<? extends EntityLivingBase> entity, LootTable lootTable);

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
