package net.silentautopsy.betternetherambientmobs.registry;

import net.silentautopsy.betternetherambientmobs.BetterNetherAmbientMobs;
import net.silentautopsy.betternetherambientmobs.world.NetherBiome;
import org.betterx.bclib.api.v2.levelgen.biomes.BCLBiomeRegistry;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;

public class NetherBiomes
{
    public static void register()
    {
        BCLBiomeRegistry.registerBiomeCodec(BetterNetherAmbientMobs.id("biome"), NetherBiome.KEY_CODEC);
        BiomeAPI.registerNetherBiomeModification((biomeID, biome) -> {
            if (!biomeID.getNamespace().equals(BetterNetherAmbientMobs.MOD_ID)) {
                NetherEntities.modifyNonBNBiome(biomeID, biome);
            }
        });
    }
}
