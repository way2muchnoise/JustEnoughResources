package jeresources.compatibility.minecraft;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import jeresources.api.render.IMobRenderHook;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.monster.Shulker;

public class RenderHooks {
    public static final IMobRenderHook ENDER_DRAGON = (IMobRenderHook<EnderDragon>) (renderInfo, entity) ->
    {
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.mulPose(Vector3f.XP.rotationDegrees(20.0F));
        modelViewStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        renderInfo.pitch = -renderInfo.pitch - 80;
        modelViewStack.mulPose(Vector3f.YN.rotationDegrees(((float)(renderInfo.yaw < 90 ? (renderInfo.yaw < -90 ? 90 : -renderInfo.yaw) : -90) / 2.0F)));
        return renderInfo;
    };

    public static final IMobRenderHook BAT = (IMobRenderHook<Bat>) (renderInfo, entity) ->
    {
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.mulPose(Vector3f.XP.rotationDegrees(20.0F));
        modelViewStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        renderInfo.pitch = -renderInfo.pitch;
        return renderInfo;
    };

    public static final IMobRenderHook ELDER_GUARDIAN = (IMobRenderHook<ElderGuardian>) (renderInfo, entity) ->
    {
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.scale(0.6F, 0.6F, 0.6F);
        // entity.setGhost();
        return renderInfo;
    };

    public static final IMobRenderHook SQUID = (IMobRenderHook<Squid>) (renderInfo, entity) ->
    {
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.mulPose(Vector3f.XP.rotationDegrees(50.0F));
        return renderInfo;
    };

    public static final IMobRenderHook GIANT = (IMobRenderHook<Giant>) (renderInfo, entity) ->
    {
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.translate(0.0F, 2.0F, 0.0F);
        modelViewStack.scale(0.7F, 0.7F, 0.7F);
        return renderInfo;
    };

    public static final IMobRenderHook SHULKER = (IMobRenderHook<Shulker>) (renderInfo, entity) ->
    {
        // TODO make Shulker peek
        return renderInfo;
    };

    public static final IMobRenderHook GROUP_FISH = (IMobRenderHook<AbstractSchoolingFish>) (renderInfo, entity) ->
    {
        PoseStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.translate(-0.1F, -0.5F, 0.0F);
        modelViewStack.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
        double pitch = renderInfo.pitch;
        renderInfo.pitch = renderInfo.yaw;
        renderInfo.yaw = - pitch;
        return renderInfo;
    };
}
