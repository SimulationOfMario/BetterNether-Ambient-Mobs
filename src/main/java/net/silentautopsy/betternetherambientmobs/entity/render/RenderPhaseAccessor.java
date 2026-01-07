package net.silentautopsy.betternetherambientmobs.entity.render;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.silentautopsy.betternetherambientmobs.mixin.client.RenderLayerMixin;
import java.util.function.Function;

public abstract class RenderPhaseAccessor extends RenderStateShard
{
    public RenderPhaseAccessor(String name, Runnable beginAction, Runnable endAction)
    {
        super(name, beginAction, endAction);
    }

    protected static final TransparencyStateShard ALPHA_ADD_TRANSPARENCY = new TransparencyStateShard(
            "alpha_transparency",
            () -> {
                RenderSystem.enableBlend();
                RenderSystem.blendFuncSeparate(
                        GlStateManager.SourceFactor.SRC_ALPHA,
                        GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                        GlStateManager.SourceFactor.ONE,
                        GlStateManager.DestFactor.ZERO
                );
            },
            () -> {
                RenderSystem.disableBlend();
                RenderSystem.defaultBlendFunc();
            }
    );

    private static RenderType getFireflySetup(ResourceLocation texture)
    {
        RenderType.CompositeState multiPhaseParameters = RenderType.CompositeState.builder()
          .setShaderState(RenderStateShard.RENDERTYPE_TRANSLUCENT_SHADER)
          .setTextureState(new TextureStateShard(texture, false, false))
          .setWriteMaskState(COLOR_WRITE)
          .setCullState(NO_CULL)
          .setOverlayState(OVERLAY)
          .setLightmapState(LIGHTMAP)
          .setTransparencyState(ALPHA_ADD_TRANSPARENCY)
          .createCompositeState(false);

        return RenderLayerMixin.callCreate("firefly",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                false,
                true,
                multiPhaseParameters
        );
    }

    private static final Function<ResourceLocation, RenderType> FIREFLY_RENDER_LAYER = Util.memoize(RenderPhaseAccessor::getFireflySetup);

    public static RenderType getFirefly(ResourceLocation texture)
    {
        return FIREFLY_RENDER_LAYER.apply(texture);
    }
}
