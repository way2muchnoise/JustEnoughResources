package jeresources.compatibility.minecraft;

import jeresources.api.render.IMobRenderHook;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.passive.EntityBat;
import net.minecraft.entity.passive.EntitySquid;

public class RenderHooks
{
    public static final IMobRenderHook ENDER_DRAGON = new IMobRenderHook<EntityDragon>()
    {
        @Override
        public IMobRenderHook.RenderInfo transform(IMobRenderHook.RenderInfo renderInfo, EntityDragon entity)
        {
            GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            renderInfo.pitch = -renderInfo.pitch - 80;
            GlStateManager.rotate(renderInfo.yaw < 90 ? (renderInfo.yaw < -90 ? 90 : -renderInfo.yaw) : -90, 0.0F, 1.0F, 0.0F);
            return renderInfo;
        }
    };

    public static final IMobRenderHook BAT = new IMobRenderHook<EntityBat>()
    {
        @Override
        public IMobRenderHook.RenderInfo transform(IMobRenderHook.RenderInfo renderInfo, EntityBat entity)
        {
            GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
            renderInfo.pitch = -renderInfo.pitch;
            return renderInfo;
        }
    };

    public static final IMobRenderHook ELDER_GUARDIAN = new IMobRenderHook<EntityGuardian>()
    {
        @Override
        public RenderInfo transform(RenderInfo renderInfo, EntityGuardian entity)
        {
            if (entity.isElder())
                GlStateManager.scale(0.6F, 0.6F, 0.6F);
            return renderInfo;
        }
    };

    public static final IMobRenderHook SQUID = new IMobRenderHook<EntitySquid>()
    {
        @Override
        public RenderInfo transform(RenderInfo renderInfo, EntitySquid entity)
        {
            GlStateManager.rotate(50.0F, 1.0F, 0.0F, 0.0F);
            return renderInfo;
        }
    };
}
