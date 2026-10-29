package jeresources.util;

import com.google.common.collect.MapMaker;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import jeresources.api.render.IMobRenderHook;
import jeresources.compatibility.api.MobRegistryImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.renderer.entity.state.BatRenderState;
import net.minecraft.client.renderer.entity.state.EnderDragonRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.GuardianRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.SquidRenderState;
import net.minecraft.client.renderer.entity.state.WitherRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.Mth;
import net.minecraft.util.Util;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.animal.fish.WaterAnimal;
import net.minecraft.world.item.ItemStack;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public final class EntityPreviewRendering {
    private static final Map<LivingEntity, PreviewFrame> FRAMES = new MapMaker().weakKeys().makeMap();

    private EntityPreviewRendering() {
    }

    public static void render(GuiGraphicsExtractor graphics, LivingEntity entity, double mouseX, double mouseY) {
        PreviewGeometryBounds bounds = frame(entity);
        int hookScale = Math.max(1, Math.round(bounds.scale()));
        IMobRenderHook.RenderInfo info = MobRegistryImpl.applyRenderHooks(new PoseStack(), entity, new IMobRenderHook.RenderInfo(0, 0, hookScale, (float)mouseX, (float)mouseY));
        float yaw = (float)Math.atan((36.5F - Mth.clamp(info.mouseX, 10, 63)) / 40.0F);
        float pitch = (float)Math.atan((80.5F - Mth.clamp(info.mouseY, 55, 99)) / 40.0F);
        EntityRenderState state = state(entity, (Util.getMillis() % 50_000_000L) / 50.0F, yaw, pitch);
        Quaternionf camera = new Quaternionf().rotateX(pitch * 20.0F * Mth.DEG_TO_RAD);
        Quaternionf rotation = new Quaternionf().rotateZ(Mth.PI).mul(camera);
        ScreenRectangle rectangle = new ScreenRectangle(8 + info.x, 44 + info.y, 57, 73).transformAxisAligned(graphics.pose());
        graphics.entity(state, (info.scale == hookScale ? bounds.scale() : info.scale) * rectangle.width() / 57.0F, new Vector3f(-bounds.centerX(), -bounds.centerY(), 0), rotation, camera, rectangle.left(), rectangle.top(), rectangle.right(), rectangle.bottom());
    }

    private static PreviewGeometryBounds frame(LivingEntity entity) {
        var dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        Object renderer = dispatcher.getRenderer(entity);
        PreviewFrame cached = FRAMES.get(entity);
        if (cached != null && cached.matches(entity, renderer)) {
            return cached.bounds();
        }
        PreviewGeometryBounds bounds = new PreviewGeometryBounds();
        for (int sample = 0; sample <= 256; sample += 4) {
            for (int horizontal = -1; horizontal <= 1; horizontal++) {
                for (int vertical = -1; vertical <= 1; vertical++) {
                    float pitch = vertical * 0.6F;
                    EntityRenderState state = state(entity, sample, horizontal * 0.6F, pitch);
                    Quaternionf camera = new Quaternionf().rotateX(pitch * 20.0F * Mth.DEG_TO_RAD);
                    PoseStack poses = new PoseStack();
                    poses.scale(1, 1, -1);
                    poses.mulPose(new Quaternionf().rotateZ(Mth.PI).mul(camera));
                    CameraRenderState view = new CameraRenderState();
                    view.orientation = camera.conjugate(new Quaternionf()).rotateY(Mth.PI);
                    PreviewGeometryBounds sampleBounds = new PreviewGeometryBounds();
                    dispatcher.submit(state, view, 0, 0, 0, poses, new PreviewSubmitNodeCollector(sampleBounds));
                    sampleBounds.includeFallback(poses.last().pose(), entity.getBbWidth(), entity.getBbHeight());
                    bounds.include(sampleBounds);
                }
            }
        }
        List<ItemStack> equipment = Arrays.stream(EquipmentSlot.values()).map(slot -> entity.getItemBySlot(slot).copy()).toList();
        FRAMES.put(entity, new PreviewFrame(renderer, entity.getBbWidth(), entity.getBbHeight(), equipment, bounds));
        return bounds;
    }

    private static EntityRenderState state(LivingEntity entity, float age, float yaw, float pitch) {
        var renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
        EntityRenderState state = renderer.createRenderState(entity, 1.0F);
        state.ageInTicks = age;
        state.lightCoords = 15728880;
        state.shadowPieces.clear();
        state.outlineColor = 0;
        if (state instanceof LivingEntityRenderState living) {
            living.bodyRot = 180.0F + yaw * 20.0F;
            living.yRot = yaw * 20.0F;
            living.xRot = -pitch * 20.0F;
            living.walkAnimationPos = age * 0.25F;
            living.walkAnimationSpeed = 0.25F;
            living.isInWater = entity instanceof WaterAnimal || entity instanceof Axolotl;
        }
        if (state instanceof GuardianRenderState guardian) {
            guardian.spikesAnimation = 1.0F;
            guardian.tailAnimation = age * 0.125F;
        }
        if (state instanceof WitherRenderState wither) {
            Arrays.fill(wither.yHeadRots, wither.bodyRot + wither.yRot);
            Arrays.fill(wither.xHeadRots, wither.xRot);
        }
        if (state instanceof SquidRenderState squid) {
            squid.tentacleAngle = Math.abs(Mth.sin(age * 0.1F)) * Mth.PI * 0.25F;
        }
        if (state instanceof BatRenderState bat) {
            bat.isResting = false;
            bat.restAnimationState.stop();
            bat.flyAnimationState.start(0);
        }
        if (state instanceof EnderDragonRenderState dragon) {
            dragon.flapTime = age * 0.1F;
            for (int index = 0; index < 64; index++) {
                dragon.flightHistory.record(0, yaw * 40.0F);
            }
        }
        return state;
    }

    private record PreviewFrame(Object renderer, float width, float height, List<ItemStack> equipment, PreviewGeometryBounds bounds) {
        private boolean matches(LivingEntity entity, Object currentRenderer) {
            if (renderer != currentRenderer || width != entity.getBbWidth() || height != entity.getBbHeight()) {
                return false;
            }
            for (EquipmentSlot slot : EquipmentSlot.values()) {
                if (!ItemStack.matches(equipment.get(slot.ordinal()), entity.getItemBySlot(slot))) {
                    return false;
                }
            }
            return true;
        }
    }
}
