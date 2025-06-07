package jeresources.compatibility.minecraft;

import com.mojang.math.Axis;
import jeresources.api.render.IMobRenderHook;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.ElderGuardian;
import net.minecraft.world.entity.monster.Giant;
import net.minecraft.world.entity.monster.Shulker;

public class RenderHooks {
    public static final IMobRenderHook ENDER_DRAGON = (IMobRenderHook<EnderDragon>) (mobPoseStack, renderInfo, entity) ->
    {
        mobPoseStack.rotateAround(Axis.XP.rotationDegrees(20.0F), 0, 0, 0);
        mobPoseStack.rotateAround(Axis.YP.rotationDegrees(180.0F), 0, 0, 0);
        renderInfo.pitch = -renderInfo.pitch - 80;
        mobPoseStack.rotateAround(Axis.YN.rotationDegrees(((float)(renderInfo.yaw < 90 ? (renderInfo.yaw < -90 ? 90 : -renderInfo.yaw) : -90) / 2.0F)), 0, 0, 0);
        return renderInfo;
    };

    public static final IMobRenderHook BAT = (IMobRenderHook<Bat>) (mobPoseStack, renderInfo, entity) ->
    {
        mobPoseStack.rotateAround(Axis.XP.rotationDegrees(20.0F), 0, 0, 0);
        mobPoseStack.rotateAround(Axis.YP.rotationDegrees(180.0F), 0, 0, 0);
        renderInfo.pitch = -renderInfo.pitch;
        return renderInfo;
    };

    public static final IMobRenderHook ELDER_GUARDIAN = (IMobRenderHook<ElderGuardian>) (mobPoseStack, renderInfo, entity) ->
    {
        mobPoseStack.scale(0.6F, 0.6F, 0.6F);
        // entity.setGhost();
        return renderInfo;
    };

    public static final IMobRenderHook SQUID = (IMobRenderHook<Squid>) (mobPoseStack, renderInfo, entity) ->
    {
        mobPoseStack.rotateAround(Axis.XP.rotationDegrees(50.0F), 0, 0, 0);
        return renderInfo;
    };

    public static final IMobRenderHook GIANT = (IMobRenderHook<Giant>) (mobPoseStack, renderInfo, entity) ->
    {
        mobPoseStack.translate(0.0F, 2.0F, 0.0F);
        mobPoseStack.scale(0.7F, 0.7F, 0.7F);
        return renderInfo;
    };

    public static final IMobRenderHook SHULKER = (IMobRenderHook<Shulker>) (mobPoseStack, renderInfo, entity) ->
    {
        // TODO make Shulker peek
        return renderInfo;
    };

    public static final IMobRenderHook GROUP_FISH = (IMobRenderHook<AbstractSchoolingFish>) (mobPoseStack, renderInfo, entity) ->
    {
        mobPoseStack.translate(-0.1F, -0.5F, 0.0F);
        mobPoseStack.rotateAround(Axis.ZP.rotationDegrees(90.0F), 0, 0, 0);
        double pitch = renderInfo.pitch;
        renderInfo.pitch = renderInfo.yaw;
        renderInfo.yaw = - pitch;
        return renderInfo;
    };
}
