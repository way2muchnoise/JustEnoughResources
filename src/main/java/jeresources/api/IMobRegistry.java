package jeresources.api;

import jeresources.api.conditionals.LightLevel;
import jeresources.api.drop.DropItem;
import jeresources.api.render.IMobRenderHook;
import net.minecraft.entity.EntityLivingBase;

public interface IMobRegistry
{
    void registerMob(EntityLivingBase entity, LightLevel lightLevel, int minExp, int maxExp, String[] biomes, DropItem... drops);
    void registerMob(EntityLivingBase entity, LightLevel lightLevel, String[] biomes, DropItem... drops);
    void registerMob(EntityLivingBase entity, LightLevel lightLevel, int exp, String[] biomes, DropItem... drops);
    void registerMob(EntityLivingBase entity, LightLevel lightLevel, int exp, DropItem... drops);
    void registerMob(EntityLivingBase entity, LightLevel lightLevel, int minExp, int maxExp, DropItem... drops);

    void registerDrops(Class<? extends EntityLivingBase> entity, DropItem... drops);

    void registerRenderHook(Class<? extends EntityLivingBase> entity, IMobRenderHook renderHook);
}
