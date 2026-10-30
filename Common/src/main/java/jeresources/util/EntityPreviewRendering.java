package jeresources.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import jeresources.api.render.IMobRenderHook;
import jeresources.compatibility.api.MobRegistryImpl;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Guardian;
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

    private static final Map<LivingEntity, Long> LAST_TICKS = new WeakHashMap<>();
    private static final Map<LivingEntity, PreviewFrame> FRAMES = new WeakHashMap<>();
    private EntityPreviewRendering() {
    }

    public static void render(GuiGraphics graphics, LivingEntity entity, double mouseX, double mouseY) {
        graphics.flush();
        RenderSystem.enableDepthTest();
        long now = Util.getMillis() / MILLIS_PER_TICK;
        Long previous = LAST_TICKS.put(entity, now);
        int steps = previous == null ? 1 : (int)Mth.clamp(now - previous, 0, 5);
        entity.tickCount = (int)(now % ANIMATION_PERIOD_TICKS);
        for (int step = 0; step < steps; step++) {
            entity.walkAnimation.update(0.25F, 0.4F);
        }
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        Quaternionf oldOrientation = new Quaternionf(dispatcher.cameraOrientation());
        Vector3f topLeft = graphics.pose().last().pose().transformPosition(new Vector3f(PREVIEW_X, PREVIEW_Y, 0));
        Vector3f bottomRight = graphics.pose().last().pose().transformPosition(new Vector3f(PREVIEW_X + PREVIEW_WIDTH, PREVIEW_Y + PREVIEW_HEIGHT, 0));
        graphics.enableScissor(Mth.floor(topLeft.x), Mth.floor(topLeft.y), Mth.ceil(bottomRight.x), Mth.ceil(bottomRight.y));
        try {
            renderModel(graphics, entity, mouseX, mouseY);
        } finally {
            graphics.disableScissor();
            dispatcher.overrideCameraOrientation(oldOrientation);
        }
    }

    private static void renderModel(GuiGraphics graphics, LivingEntity entity, double mouseX, double mouseY) {
        float yaw = (float)Math.atan((PREVIEW_CENTER_X - Mth.clamp(mouseX, MOUSE_MIN_X, MOUSE_MAX_X)) / MOUSE_DISTANCE_SCALE);
        float pitch = (float)Math.atan((PREVIEW_CENTER_Y - Mth.clamp(mouseY, MOUSE_MIN_Y, MOUSE_MAX_Y)) / MOUSE_DISTANCE_SCALE);
        float body = entity.yBodyRot;
        float rotation = entity.getYRot();
        float tilt = entity.getXRot();
        float head = entity.yHeadRot;
        float previousHead = entity.yHeadRotO;
        int ticks = entity.tickCount;
        Quaternionf camera = new Quaternionf().rotateX(pitch * POSE_ROTATION_DEGREES * Mth.DEG_TO_RAD);
        Quaternionf pose = new Quaternionf().rotateZ(Mth.PI).mul(camera);
        try {
            PreviewGeometryBounds bounds = frame(entity);
            IMobRenderHook.RenderInfo info = MobRegistryImpl.applyRenderHooks(entity, new IMobRenderHook.RenderInfo(Math.round(PREVIEW_CENTER_X), Math.round(PREVIEW_CENTER_Y), bounds.scale(), PREVIEW_CENTER_X - mouseX, PREVIEW_CENTER_Y - mouseY));
            yaw = (float)Math.atan(Mth.clamp(info.yaw, PREVIEW_CENTER_X - MOUSE_MAX_X, PREVIEW_CENTER_X - MOUSE_MIN_X) / MOUSE_DISTANCE_SCALE);
            pitch = (float)Math.atan(Mth.clamp(info.pitch, PREVIEW_CENTER_Y - MOUSE_MAX_Y, PREVIEW_CENTER_Y - MOUSE_MIN_Y) / MOUSE_DISTANCE_SCALE);
            camera = new Quaternionf().rotateX(pitch * POSE_ROTATION_DEGREES * Mth.DEG_TO_RAD);
            pose = new Quaternionf().rotateZ(Mth.PI).mul(camera);
            applyPose(entity, ticks, yaw, pitch);
            // Render hooks use integer centers; restore the half-pixel center of the odd-sized frame.
            InventoryScreen.renderEntityInInventory(graphics, info.x - 0.5F, info.y - 0.5F, (float)info.scale, new Vector3f(-bounds.centerX(), -bounds.centerY(), 0), pose, camera, entity);
        } finally {
            entity.yBodyRot = body;
            entity.setYRot(rotation);
            entity.setXRot(tilt);
            entity.yHeadRot = head;
            entity.yHeadRotO = previousHead;
        }
    }

    private static PreviewGeometryBounds frame(LivingEntity entity) {
        EntityRenderer<? super LivingEntity> renderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(entity);
        PreviewFrame cached = FRAMES.get(entity);
        if (cached != null && cached.matches(entity, renderer)) {
            return cached.bounds();
        }
        int ticks = entity.tickCount;
        PreviewGeometryBounds bounds = new PreviewGeometryBounds();
        if (entity instanceof Bat bat) {
            bat.flyAnimationState.start(0);
        }
        try {
            // Sample animation and cursor extremes once so the scale stays stable while the model moves.
            for (int sample = 0; sample <= FIT_ANIMATION_TICKS; sample += FIT_SAMPLE_INTERVAL) {
                for (int step = 0; step < FIT_SAMPLE_INTERVAL; step++) {
                    entity.walkAnimation.update(0.25F, 1.0F);
                }
                for (int horizontal = -1; horizontal <= 1; horizontal++) {
                    for (int vertical = -1; vertical <= 1; vertical++) {
                        float pitch = vertical * FIT_ANGLE_LIMIT;
                        applyPose(entity, sample, horizontal * FIT_ANGLE_LIMIT, pitch);
                        Quaternionf camera = new Quaternionf().rotateX(pitch * POSE_ROTATION_DEGREES * Mth.DEG_TO_RAD);
                        bounds.include(measure(entity, new Quaternionf().rotateZ(Mth.PI).mul(camera), camera));
                    }
                }
            }
        } finally {
            entity.tickCount = ticks;
            if (entity instanceof Bat bat) {
                bat.flyAnimationState.start(ticks);
            }
        }
        List<ItemStack> equipment = Arrays.stream(EquipmentSlot.values()).map(slot -> entity.getItemBySlot(slot).copy()).toList();
        FRAMES.put(entity, new PreviewFrame(renderer, entity.getBbWidth(), entity.getBbHeight(), equipment, bounds));
        return bounds;
    }

    private static void applyPose(LivingEntity entity, int ticks, float yaw, float pitch) {
        entity.tickCount = ticks;
        entity.yBodyRot = 180.0F + yaw * POSE_ROTATION_DEGREES;
        entity.setYRot(180.0F + yaw * (2.0F * POSE_ROTATION_DEGREES));
        entity.setXRot(-pitch * POSE_ROTATION_DEGREES);
        entity.yHeadRot = entity.getYRot();
        entity.yHeadRotO = entity.getYRot();
        animateSpecialModel(entity, yaw);
    }

    private record PreviewFrame(EntityRenderer<?> renderer, float width, float height, List<ItemStack> equipment, PreviewGeometryBounds bounds) {
        private boolean matches(LivingEntity entity, EntityRenderer<?> currentRenderer) {
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

    private static void animateSpecialModel(LivingEntity entity, float yaw) {
        if (entity instanceof WaterAnimal || entity instanceof Axolotl) {
            entity.wasTouchingWater = true;
        }
        if (entity instanceof Guardian guardian) {
            guardian.clientSideSpikesAnimation = 1.0F;
            guardian.clientSideSpikesAnimationO = 1.0F;
            guardian.clientSideTailAnimation = (entity.tickCount + 1) * 0.125F;
            guardian.clientSideTailAnimationO = guardian.clientSideTailAnimation;
        }
        if (entity instanceof WitherBoss wither) {
            Arrays.fill(wither.yRotHeads, wither.yHeadRot);
            Arrays.fill(wither.xRotHeads, wither.getXRot());
        }
        if (entity instanceof Squid squid) {
            squid.tentacleAngle = Mth.abs(Mth.sin(entity.tickCount * 0.1F)) * Mth.PI * 0.25F;
            squid.oldTentacleAngle = squid.tentacleAngle;
        }
        if (entity instanceof Bat bat) {
            bat.setResting(false);
            bat.restAnimationState.stop();
            bat.flyAnimationState.startIfStopped(entity.tickCount);
        }
        if (entity instanceof EnderDragon dragon) {
            dragon.flapTime = entity.tickCount * 0.1F;
            dragon.oFlapTime = dragon.flapTime;
            for (double[] position : dragon.positions) {
                position[0] = yaw * (2.0F * POSE_ROTATION_DEGREES);
            }
        }
    }

    private static PreviewGeometryBounds measure(LivingEntity entity, Quaternionf rotation, Quaternionf camera) {
        PreviewGeometryBounds bounds = new PreviewGeometryBounds();
        PoseStack poses = new PoseStack();
        poses.scale(1, 1, -1);
        poses.mulPose(rotation);
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();
        dispatcher.overrideCameraOrientation(camera.conjugate(new Quaternionf()).rotateY(Mth.PI));
        dispatcher.setRenderShadow(false);
        try {
            dispatcher.render(entity, 0, 0, 0, 0, 1, poses, bounds, LightTexture.FULL_BRIGHT);
        } finally {
            dispatcher.setRenderShadow(true);
        }
        bounds.includeFallback(poses.last().pose(), entity.getBbWidth(), entity.getBbHeight());
        return bounds;
    }

}
