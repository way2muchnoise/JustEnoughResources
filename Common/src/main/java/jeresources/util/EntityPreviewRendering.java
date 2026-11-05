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
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.state.BatRenderState;
import net.minecraft.client.renderer.entity.state.EnderDragonRenderState;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.entity.state.GuardianRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.entity.state.SquidRenderState;
import net.minecraft.client.renderer.entity.state.WitherRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.LightCoordsUtil;
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
    // Dimensions of the model area in recipe-local GUI coordinates.
    public static final int PREVIEW_WIDTH = 57;
    public static final int PREVIEW_HEIGHT = 73;
    private static final int PREVIEW_X = 8;
    private static final int PREVIEW_Y = 44;
    private static final float PREVIEW_CENTER_X = PREVIEW_X + PREVIEW_WIDTH * 0.5F;
    private static final float PREVIEW_CENTER_Y = PREVIEW_Y + PREVIEW_HEIGHT * 0.5F;

    // Clamp cursor tracking to keep the pose within the range sampled for fitting.
    private static final float MOUSE_MIN_X = 10.0F;
    private static final float MOUSE_MAX_X = 63.0F;
    private static final float MOUSE_MIN_Y = 55.0F;
    private static final float MOUSE_MAX_Y = 99.0F;
    private static final float MOUSE_DISTANCE_SCALE = 40.0F;
    private static final float POSE_ROTATION_DEGREES = 20.0F;
    private static final float FIT_ANGLE_LIMIT = 0.6F;
    private static final int FIT_ANIMATION_TICKS = 256;
    private static final int FIT_SAMPLE_INTERVAL = 4;
    private static final long MILLIS_PER_TICK = 50L;
    private static final int ANIMATION_PERIOD_TICKS = 1_000_000;

    private static final Map<LivingEntity, PreviewFrame> FRAMES = new MapMaker().weakKeys().makeMap();

    private EntityPreviewRendering() {
    }

    public static void render(GuiGraphicsExtractor graphics, LivingEntity entity, double mouseX, double mouseY) {
        PreviewGeometryBounds bounds = frame(entity);
        int hookScale = Math.max(1, Math.round(bounds.scale()));
        IMobRenderHook.RenderInfo info = MobRegistryImpl.applyRenderHooks(new PoseStack(), entity, new IMobRenderHook.RenderInfo(0, 0, hookScale, (float)mouseX, (float)mouseY));
        float yaw = (float)Math.atan((PREVIEW_CENTER_X - Mth.clamp(info.mouseX, MOUSE_MIN_X, MOUSE_MAX_X)) / MOUSE_DISTANCE_SCALE);
        float pitch = (float)Math.atan((PREVIEW_CENTER_Y - Mth.clamp(info.mouseY, MOUSE_MIN_Y, MOUSE_MAX_Y)) / MOUSE_DISTANCE_SCALE);
        EntityRenderState state = state(entity, (Util.getMillis() % (ANIMATION_PERIOD_TICKS * MILLIS_PER_TICK)) / (float)MILLIS_PER_TICK, yaw, pitch);
        Quaternionf camera = new Quaternionf().rotateX(pitch * POSE_ROTATION_DEGREES * Mth.DEG_TO_RAD);
        Quaternionf rotation = new Quaternionf().rotateZ(Mth.PI).mul(camera);
        ScreenRectangle rectangle = new ScreenRectangle(PREVIEW_X + info.x, PREVIEW_Y + info.y, PREVIEW_WIDTH, PREVIEW_HEIGHT).transformAxisAligned(graphics.pose());
        graphics.entity(state, (info.scale == hookScale ? bounds.scale() : info.scale) * rectangle.width() / (float)PREVIEW_WIDTH, new Vector3f(-bounds.centerX(), -bounds.centerY(), 0), rotation, camera, rectangle.left(), rectangle.top(), rectangle.right(), rectangle.bottom());
    }

    private static PreviewGeometryBounds frame(LivingEntity entity) {
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        EntityRenderer<? super LivingEntity, ?> renderer = dispatcher.getRenderer(entity);
        PreviewFrame cached = FRAMES.get(entity);
        if (cached != null && cached.matches(entity, renderer)) {
            return cached.bounds();
        }
        PreviewGeometryBounds bounds = new PreviewGeometryBounds();
        // Sample animation and cursor extremes once so the scale stays stable while the model moves.
        for (int sample = 0; sample <= FIT_ANIMATION_TICKS; sample += FIT_SAMPLE_INTERVAL) {
            for (int horizontal = -1; horizontal <= 1; horizontal++) {
                for (int vertical = -1; vertical <= 1; vertical++) {
                    float pitch = vertical * FIT_ANGLE_LIMIT;
                    EntityRenderState state = state(entity, sample, horizontal * FIT_ANGLE_LIMIT, pitch);
                    Quaternionf camera = new Quaternionf().rotateX(pitch * POSE_ROTATION_DEGREES * Mth.DEG_TO_RAD);
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
        EntityRenderer<? super LivingEntity, ?> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
        EntityRenderState state = renderer.createRenderState(entity, 1.0F);
        state.ageInTicks = age;
        state.lightCoords = LightCoordsUtil.FULL_BRIGHT;
        state.shadowPieces.clear();
        state.outlineColor = 0;
        if (state instanceof LivingEntityRenderState living) {
            living.bodyRot = 180.0F + yaw * POSE_ROTATION_DEGREES;
            living.yRot = yaw * POSE_ROTATION_DEGREES;
            living.xRot = -pitch * POSE_ROTATION_DEGREES;
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
                dragon.flightHistory.record(0, yaw * (2.0F * POSE_ROTATION_DEGREES));
            }
        }
        return state;
    }

    private record PreviewFrame(EntityRenderer<?, ?> renderer, float width, float height, List<ItemStack> equipment, PreviewGeometryBounds bounds) {
        private boolean matches(LivingEntity entity, EntityRenderer<?, ?> currentRenderer) {
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
