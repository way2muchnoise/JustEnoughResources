package jeresources.compatibility.minecraft;

import jeresources.api.render.IMobRenderHook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityElderGuardian;
import net.minecraft.entity.monster.EntityGiantZombie;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;

public class RenderHooks {
    public static final IMobRenderHook ENDER_DRAGON = (IMobRenderHook<EntityDragon>) (renderInfo, entity) ->
    {
        GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        renderInfo.pitch = -renderInfo.pitch - 80;
        GlStateManager.rotate(renderInfo.yaw < 90 ? (renderInfo.yaw < -90 ? 90 : -renderInfo.yaw) : -90, 0.0F, 1.0F, 0.0F);
        return renderInfo;
    };

    public static final IMobRenderHook BAT = (IMobRenderHook<EntityBat>) (renderInfo, entity) ->
    {
        GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        renderInfo.pitch = -renderInfo.pitch;
        return renderInfo;
    };

    public static final IMobRenderHook ELDER_GUARDIAN = (IMobRenderHook<EntityElderGuardian>) (renderInfo, entity) ->
    {
        GlStateManager.scale(0.6F, 0.6F, 0.6F);
        entity.setGhost();
        return renderInfo;
    };

    public static final IMobRenderHook SQUID = (IMobRenderHook<EntitySquid>) (renderInfo, entity) ->
    {
        GlStateManager.rotate(50.0F, 1.0F, 0.0F, 0.0F);
        return renderInfo;
    };

    public static final IMobRenderHook GIANT = (IMobRenderHook<EntityGiantZombie>) (renderInfo, entity) ->
    {
        GlStateManager.translate(0.0F, -2.0F, 0.0F);
        GlStateManager.scale(0.7F, 0.7F, 0.7F);
        return renderInfo;
    };

    public static final IMobRenderHook SHULKER = (IMobRenderHook<EntityShulker>) (renderInfo, entity) ->
    {
        return renderInfo;
    };
}
