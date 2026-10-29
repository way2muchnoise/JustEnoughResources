package jeresources.util;

import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;
import org.joml.Vector3f;

final class PreviewGeometryBounds implements VertexConsumer {
    private float minX = Float.POSITIVE_INFINITY;
    private float maxX = Float.NEGATIVE_INFINITY;
    private float minY = Float.POSITIVE_INFINITY;
    private float maxY = Float.NEGATIVE_INFINITY;

    PreviewGeometryBounds() {
    }

    void include(PreviewGeometryBounds other) {
        addVertex(other.minX, other.minY, 0);
        addVertex(other.maxX, other.maxY, 0);
    }

    void includeFallback(Matrix4f transform, float width, float height) {
        if (Float.isFinite(minX)) {
            return;
        }
        float halfWidth = Math.max(0.1F, width) * 0.5F;
        for (int corner = 0; corner < 8; corner++) {
            Vector3f point = transform.transformPosition(new Vector3f((corner & 1) == 0 ? -halfWidth : halfWidth, (corner & 2) == 0 ? 0 : Math.max(0.1F, height), (corner & 4) == 0 ? -halfWidth : halfWidth));
            addVertex(point.x, point.y, point.z);
        }
    }

    float scale() {
        return Float.isFinite(minX) ? Math.min(49.0F / Math.max(0.01F, maxX - minX), 65.0F / Math.max(0.01F, maxY - minY)) : 1.0F;
    }

    float centerX() {
        return Float.isFinite(minX) ? (minX + maxX) * 0.5F : 0.0F;
    }

    float centerY() {
        return Float.isFinite(minY) ? (minY + maxY) * 0.5F : 0.0F;
    }

    @Override
    public VertexConsumer addVertex(float x, float y, float z) {
        if (Float.isFinite(x) && Float.isFinite(y)) {
            minX = Math.min(minX, x);
            maxX = Math.max(maxX, x);
            minY = Math.min(minY, y);
            maxY = Math.max(maxY, y);
        }
        return this;
    }

    @Override
    public VertexConsumer setColor(int red, int green, int blue, int alpha) {
        return this;
    }

    @Override
    public VertexConsumer setUv(float u, float v) {
        return this;
    }

    @Override
    public VertexConsumer setUv1(int u, int v) {
        return this;
    }

    @Override
    public VertexConsumer setUv2(int u, int v) {
        return this;
    }

    @Override
    public VertexConsumer setNormal(float normalX, float normalY, float normalZ) {
        return this;
    }

    @Override
    public VertexConsumer setColor(int color) {
        return this;
    }

    @Override
    public VertexConsumer setLineWidth(float width) {
        return this;
    }

    @Override
    public VertexConsumer setUv3(float u, float v) {
        return this;
    }
}
