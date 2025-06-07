package jeresources.api.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.LivingEntity;

public interface IMobRenderHook<T extends LivingEntity> {
    class RenderInfo {
        public int x, y;
        public double scale, yaw, pitch;

        public RenderInfo(int x, int y, double scale, double yaw, double pitch) {
            this.x = x;
            this.y = y;
            this.scale = scale;
            this.yaw = yaw;
            this.pitch = pitch;
        }
    }

    /**
     * This method will be called up drawing mobs in the view
     *
     * @param mobPoseStack current {@link PoseStack} used during rendering, manipulate this to apply transformations
     * @param renderInfo   contains info about the current render context see {@link RenderInfo}
     * @param entity       the entity that will be rendered
     * @return the given {@link RenderInfo} with possible changes
     */
    RenderInfo transform(PoseStack mobPoseStack, RenderInfo renderInfo, T entity);
}
