package net.silentautopsy.betternetherambientmobs;

import net.fabricmc.api.ClientModInitializer;
import net.silentautopsy.betternetherambientmobs.registry.EntityRendersRegistry;

public class BetterNetherAmbientMobsClient implements ClientModInitializer
{
    @Override
    public void onInitializeClient()
    {
        EntityRendersRegistry.register();
    }
}
