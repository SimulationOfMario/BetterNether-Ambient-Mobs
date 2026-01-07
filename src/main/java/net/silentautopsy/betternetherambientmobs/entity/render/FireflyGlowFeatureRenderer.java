package net.silentautopsy.betternetherambientmobs.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.silentautopsy.betternetherambientmobs.entity.custom.EntityFirefly;
import net.silentautopsy.betternetherambientmobs.entity.model.ModelEntityFirefly;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

class FireflyGlowFeatureRenderer extends RenderLayer<EntityFirefly, AgeableListModel<EntityFirefly>>
{
    private static final int LIT = 15728880;

    public FireflyGlowFeatureRenderer(RenderLayerParent<EntityFirefly, AgeableListModel<EntityFirefly>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public void render(
            @NotNull PoseStack matrices,
            @NotNull MultiBufferSource vertices,
            int light,
            @NotNull EntityFirefly livingEntity,
            float limbAngle,
            float limbDistance,
            float tickDelta,
            float animationProgress,
            float headYaw,
            float headPitch
    ) {
        EntityModel<EntityFirefly> model = this.getParentModel();

        if (model instanceof ModelEntityFirefly) {
            ResourceLocation identifier = this.getTextureLocation(livingEntity);
            RenderType renderLayer = RenderPhaseAccessor.getFirefly(identifier);
            VertexConsumer vertexConsumer = vertices.getBuffer(renderLayer);

            float red = livingEntity.getRed();
            float green = livingEntity.getGreen();
            float blue = livingEntity.getBlue();

            addViewAlignedGlow(matrices, vertexConsumer, red, green, blue);

            ((ModelEntityFirefly) model).getGlowPart().render(
                    matrices,
                    vertexConsumer,
                    light,
                    OverlayTexture.NO_OVERLAY,
                    red * 2,
                    green * 2,
                    blue * 2,
                    1f
            );

            ((ModelEntityFirefly) model).getGlowPart().render(
                    matrices,
                    vertexConsumer,
                    light,
                    OverlayTexture.NO_OVERLAY,
                    red * 2,
                    green * 2,
                    blue * 2,
                    1f
            );

        }
    }

    private void addViewAlignedGlow(
            PoseStack matrices,
            VertexConsumer vertexConsumer,
            float red,
            float green,
            float blue
    ) {
        matrices.pushPose();
        matrices.translate(0, 1.25, 0);

        //Get inverse rotation to make view-aligned
        Matrix3f normalMatrix = matrices.last().normal();
        normalMatrix.transpose();
        matrices.mulPose(normalMatrix.transpose(new Matrix3f()).getNormalizedRotation(new Quaternionf()));

        PoseStack.Pose entry = matrices.last();
        Matrix4f matrix4f = entry.pose();
        Matrix3f matrix3f = entry.normal();

        addVertex(matrix4f, matrix3f, vertexConsumer, -1, -1, 0F, 0.5F, red, green, blue);
        addVertex(matrix4f, matrix3f, vertexConsumer, 1, -1, 1F, 0.5F, red, green, blue);
        addVertex(matrix4f, matrix3f, vertexConsumer, 1, 1, 1F, 1F, red, green, blue);
        addVertex(matrix4f, matrix3f, vertexConsumer, -1, 1, 0F, 1F, red, green, blue);

        matrices.popPose();
    }

    public static void addVertex(
            Matrix4f matrix4f,
            Matrix3f matrix3f,
            VertexConsumer vertexConsumer,
            float posX,
            float posY,
            float u,
            float v,
            float red,
            float green,
            float blue
    ) {
        vertexConsumer
                .vertex(matrix4f, posX, posY, 0)
                .color(red, green, blue, 1F)
                .uv(u, v)
                .overlayCoords(OverlayTexture.NO_OVERLAY)
                .uv2(LIT)
                .normal(matrix3f, 0, 1, 0).endVertex();
    }
}
