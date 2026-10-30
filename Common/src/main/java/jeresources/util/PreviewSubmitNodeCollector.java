package jeresources.util;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.OrderedSubmitNodeCollector;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.feature.phase.TranslucentFeatureRenderPhase;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.UvMapping;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.ItemQuads;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3f;

public final class PreviewSubmitNodeCollector extends SubmitNodeCollection implements SubmitNodeCollector {
    private final PreviewGeometryBounds bounds;

    public PreviewSubmitNodeCollector(PreviewGeometryBounds bounds) {
        super(false, new TranslucentFeatureRenderPhase());
        this.bounds = bounds;
    }

    @Override
    public OrderedSubmitNodeCollector order(int order) {
        return this;
    }

    @Override
    public <S> void submitModel(Model<? super S> model, S state, PoseStack poses, RenderType type, int light, int overlay, int color, UvMapping sprite, int outline) {
        model.setupAnim(state);
        model.renderToBuffer(poses, bounds, light, overlay, color);
    }

    @Override
    public void submitCustomGeometry(PoseStack poses, RenderType type, SubmitNodeCollector.CustomGeometryRenderer renderer) {
        renderer.render(poses.last(), bounds);
    }

    @Override
    public void submitItem(PoseStack poses, ItemDisplayContext context, int light, int overlay, int outline, int[] tints, ItemQuads quads, ItemStackRenderState.FoilType foil) {
        for (BakedQuad quad : quads.all()) {
            for (int vertex = 0; vertex < 4; vertex++) {
                Vector3f point = poses.last().pose().transformPosition(quad.position(vertex), new Vector3f());
                bounds.addVertex(point.x, point.y, point.z);
            }
        }
    }
}
