package net.silentautopsy.betternetherambientmobs;

import net.fabricmc.api.ClientModInitializer;
import net.silentautopsy.betternetherambientmobs.registry.EntityRenderRegistry;

public class BetterNetherAmbientMobsClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        EntityRenderRegistry.register();
    }
}
