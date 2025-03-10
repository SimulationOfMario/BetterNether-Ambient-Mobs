package net.silentautopsy.betternetherambientmobs.registry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.silentautopsy.betternetherambientmobs.BetterNetherAmbientMobs;
import net.silentautopsy.betternetherambientmobs.entity.model.ModelEntityFirefly;
import net.silentautopsy.betternetherambientmobs.entity.model.ModelHydrogenJellyfish;
import net.silentautopsy.betternetherambientmobs.entity.render.RenderFirefly;
import net.silentautopsy.betternetherambientmobs.entity.render.RenderHydrogenJellyfish;

@Environment(EnvType.CLIENT)
public class EntityRenderRegistry
{
    private static final String DEFAULT_LAYER = "main";
    public static final ModelLayerLocation FIREFLY_MODEL = registerMain("firefly");
    public static final ModelLayerLocation HYDROGEN_JELLYFISH_MODEL = registerMain("hydrogen_jelly");


    public static ModelLayerLocation registerMain(String id)
    {
        return new ModelLayerLocation(new ResourceLocation(BetterNetherAmbientMobs.MOD_ID, id), DEFAULT_LAYER);
    }

    public static void register()
    {
        registerRenderMob(NetherEntities.FIREFLY.type(), RenderFirefly.class);
        registerRenderMob(NetherEntities.HYDROGEN_JELLYFISH.type(), RenderHydrogenJellyfish.class);

        EntityModelLayerRegistry.registerModelLayer(FIREFLY_MODEL, ModelEntityFirefly::getTexturedModelData);
        EntityModelLayerRegistry.registerModelLayer(HYDROGEN_JELLYFISH_MODEL, ModelHydrogenJellyfish::getTexturedModelData);
    }

    private static void registerRenderMob(EntityType<?> entity, Class<? extends MobRenderer<?, ?>> renderer) {
        EntityRendererRegistry.register(entity, (context) -> {
            MobRenderer render = null;
            try
            {
                render = renderer.getConstructor(context.getClass()).newInstance(context);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return render;
        });
    }
}
