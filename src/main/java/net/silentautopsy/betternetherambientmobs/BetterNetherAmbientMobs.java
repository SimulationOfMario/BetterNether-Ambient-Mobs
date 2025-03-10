package net.silentautopsy.betternetherambientmobs;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;
import net.silentautopsy.betternetherambientmobs.registry.NetherEntities;
import net.silentautopsy.betternetherambientmobs.registry.*;
import net.silentautopsy.betternetherambientmobs.tab.CreativeTabs;
import org.betterx.bclib.api.v2.dataexchange.DataExchangeAPI;
import org.betterx.worlds.together.world.WorldConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterNetherAmbientMobs implements ModInitializer
{
	public static final String MOD_ID = "betternetherambientmobs";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize()
	{
		LOGGER.info("Initializing " + MOD_ID);

		SoundsRegistry.register(MOD_ID);
		SoundsRegistry.ensureStaticallyLoaded();

		NetherItems.register();

		NetherEntities.register();

		NetherBiomes.register();

		NetherTags.register();

		WorldConfig.registerModCache(MOD_ID);
		DataExchangeAPI.registerMod(MOD_ID);

		CreativeTabs.register();
	}

	public static ResourceLocation id(String path)
	{
		return BetterNetherAmbientMobs.makeID(path);
	}

	public static ResourceLocation makeID(String path)
	{
		return new ResourceLocation(MOD_ID, path);
	}
}