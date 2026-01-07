package net.silentautopsy.betternetherambientmobs;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import net.silentautopsy.betternetherambientmobs.registry.*;
import net.silentautopsy.betternetherambientmobs.registry.CreativeTabsRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterNetherAmbientMobs implements ModInitializer
{
	public static final String MOD_ID = "betternetherambientmobs";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize()
	{
		LOGGER.info("Initializing BetterNether Ambient Mobs...");

        if (FabricLoader.getInstance().isModLoaded("betternether"))
        {
            LOGGER.warn(
                """
                BetterNether Ambient Mobs must not be used alongside BetterNether!
                BetterNether already provides overlapping ambient mob systems and related content,
                which may lead to duplicated mechanics, non-expected behaviors, and unstable gameplay.
                It is strongly recommended to keep only one of the two mods installed when starting the game!
                """
            );
        }

		SoundsRegistry.register(MOD_ID);

        TagsRegistry.register();

        EntitiesRegistry.register();

		CreativeTabsRegistry.register();

        BiomesRegistry.register();
    }

	public static ResourceLocation makeID(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}
}