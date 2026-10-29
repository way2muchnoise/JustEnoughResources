package jeresources.util;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3f;

final class PreviewSubmitNodeCollector extends SubmitNodeCollection implements SubmitNodeCollector {
    private final PreviewGeometryBounds bounds;

    PreviewSubmitNodeCollector(PreviewGeometryBounds bounds) {
        super(null);
        this.bounds = bounds;
    }

    @Override
    public OrderedSubmitNodeCollector order(int order) {
        return this;
    }

    @Override
    public <S> void submitModel(Model<? super S> model, S state, PoseStack poses, RenderType type, int light, int overlay, int color, TextureAtlasSprite sprite, int outline, ModelFeatureRenderer.CrumblingOverlay crumbling) {
        model.setupAnim(state);
        model.renderToBuffer(poses, bounds, light, overlay, color);
    }

    @Override
    public void submitCustomGeometry(PoseStack poses, RenderType type, SubmitNodeCollector.CustomGeometryRenderer renderer) {
        renderer.render(poses.last(), bounds);
    }

    @Override
    public void submitItem(PoseStack poses, ItemDisplayContext context, int light, int overlay, int outline, int[] tints, List<BakedQuad> quads, ItemStackRenderState.FoilType foil) {
        for (BakedQuad quad : quads) {
            for (int vertex = 0; vertex < 4; vertex++) {
                Vector3f point = poses.last().pose().transformPosition(quad.position(vertex), new Vector3f());
                bounds.addVertex(point.x, point.y, point.z);
            }
        }
    }

    @Override
    public void submitModelPart(ModelPart part, PoseStack poses, RenderType type, int light, int overlay, TextureAtlasSprite sprite, boolean sheeted, boolean foil, int color, ModelFeatureRenderer.CrumblingOverlay crumbling, int outline) {
        part.render(poses, bounds, light, overlay, color);
    }
}
