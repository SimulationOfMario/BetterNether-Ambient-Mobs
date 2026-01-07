package net.silentautopsy.betternetherambientmobs.entity.render;

import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.silentautopsy.betternetherambientmobs.BetterNetherAmbientMobs;
import net.silentautopsy.betternetherambientmobs.entity.custom.EntityFirefly;
import net.silentautopsy.betternetherambientmobs.entity.model.ModelEntityFirefly;
import net.silentautopsy.betternetherambientmobs.registry.EntityRendersRegistry;
import org.jetbrains.annotations.NotNull;

public class RenderFirefly extends MobRenderer<EntityFirefly, AgeableListModel<EntityFirefly>>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(BetterNetherAmbientMobs.MOD_ID, "textures/entity/firefly.png");

    public RenderFirefly(EntityRendererProvider.Context ctx)
    {
        super(ctx, new ModelEntityFirefly(ctx.bakeLayer(EntityRendersRegistry.FIREFLY_MODEL)), 0);
        this.addLayer(new FireflyGlowFeatureRenderer(this));
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EntityFirefly entity) {
        return TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(@NotNull EntityFirefly entity, @NotNull BlockPos blockPos) {
        return 15;
    }
}
