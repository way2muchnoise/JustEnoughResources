package jeresources.compatibility.minecraft;

import com.mojang.blaze3d.platform.GlStateManager;
import jeresources.api.render.IMobRenderHook;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.monster.ElderGuardianEntity;
import net.minecraft.entity.monster.GiantEntity;
import net.minecraft.entity.monster.ShulkerEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.SquidEntity;

public class RenderHooks {
    public static final IMobRenderHook ENDER_DRAGON = (IMobRenderHook<EnderDragonEntity>) (renderInfo, entity) ->
    {
        GlStateManager.rotatef(20.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
        renderInfo.pitch = -renderInfo.pitch - 80;
        GlStateManager.rotatef((float)(renderInfo.yaw < 90 ? (renderInfo.yaw < -90 ? 90 : -renderInfo.yaw) : -90), 0.0F, 1.0F, 0.0F);
        return renderInfo;
    };

    public static final IMobRenderHook BAT = (IMobRenderHook<BatEntity>) (renderInfo, entity) ->
    {
        GlStateManager.rotatef(20.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.rotatef(180.0F, 0.0F, 1.0F, 0.0F);
        renderInfo.pitch = -renderInfo.pitch;
        return renderInfo;
    };

    public static final IMobRenderHook ELDER_GUARDIAN = (IMobRenderHook<ElderGuardianEntity>) (renderInfo, entity) ->
    {
        GlStateManager.scalef(0.6F, 0.6F, 0.6F);
        entity.setGhost();
        return renderInfo;
    };

    public static final IMobRenderHook SQUID = (IMobRenderHook<SquidEntity>) (renderInfo, entity) ->
    {
        GlStateManager.rotatef(50.0F, 1.0F, 0.0F, 0.0F);
        return renderInfo;
    };

    public static final IMobRenderHook GIANT = (IMobRenderHook<GiantEntity>) (renderInfo, entity) ->
    {
        GlStateManager.translatef(0.0F, -2.0F, 0.0F);
        GlStateManager.scalef(0.7F, 0.7F, 0.7F);
        return renderInfo;
    };

    public static final IMobRenderHook SHULKER = (IMobRenderHook<ShulkerEntity>) (renderInfo, entity) ->
    {
        return renderInfo;
    };
}
