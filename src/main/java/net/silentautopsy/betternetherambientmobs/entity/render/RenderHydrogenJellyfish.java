package net.silentautopsy.betternetherambientmobs.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.silentautopsy.betternetherambientmobs.BetterNetherAmbientMobs;
import net.silentautopsy.betternetherambientmobs.entity.custom.EntityHydrogenJellyfish;
import net.silentautopsy.betternetherambientmobs.entity.model.ModelHydrogenJellyfish;
import net.silentautopsy.betternetherambientmobs.registry.EntityRendersRegistry;
import org.jetbrains.annotations.NotNull;

public class RenderHydrogenJellyfish extends MobRenderer<EntityHydrogenJellyfish, AgeableListModel<EntityHydrogenJellyfish>>
{
    private static final ResourceLocation TEXTURE = new ResourceLocation(BetterNetherAmbientMobs.MOD_ID, "textures/entity/jellyfish.png");

    public RenderHydrogenJellyfish(EntityRendererProvider.Context ctx)
    {
        super(ctx, new ModelHydrogenJellyfish(ctx.bakeLayer(EntityRendersRegistry.HYDROGEN_JELLYFISH_MODEL)), 1);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EntityHydrogenJellyfish entity)
    {
        return TEXTURE;
    }

    @Override
    protected int getBlockLightLevel(@NotNull EntityHydrogenJellyfish entity, @NotNull BlockPos pos)
    {
        return 15;
    }

    @Override
    protected void scale(EntityHydrogenJellyfish entity, PoseStack matrixStack, float f)
    {
        float scale = entity.getScale();
        matrixStack.scale(scale, scale, scale);
    }
}
