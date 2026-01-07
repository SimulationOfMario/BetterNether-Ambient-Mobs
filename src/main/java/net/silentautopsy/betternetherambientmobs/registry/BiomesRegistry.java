package net.silentautopsy.betternetherambientmobs.registry;

import net.fabricmc.fabric.api.biome.v1.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.silentautopsy.betternetherambientmobs.BetterNetherAmbientMobs;
import net.silentautopsy.betternetherambientmobs.entity.EntityWrapper;

import java.util.function.Predicate;

public class BiomesRegistry
{
    public static void register()
    {
        BetterNetherAmbientMobs.LOGGER.info("Registering biome spawns...");

        BiomeModifications.create(new ResourceLocation(BetterNetherAmbientMobs.MOD_ID, "nether_spawns"))
                .add(
                    ModificationPhase.ADDITIONS,
                    BiomeSelectors.foundInTheNether(),
                    (selection, context) -> KnownSpawnTypes.applyAll(selection.getBiomeKey().location(), context)
                );

        BetterNetherAmbientMobs.LOGGER.info("Biome spawns registered successfully");
    }

    public enum KnownSpawnTypes
    {
        FIREFLY(5, 1, 3, EntitiesRegistry.FIREFLY),
        HYDROGEN_JELLYFISH(5, 2, 6, EntitiesRegistry.HYDROGEN_JELLYFISH);

        public final int weight;
        public final int minGroupSize;
        public final int maxGroupSize;
        public final EntityType<?> type;
        public final EntityWrapper<?> wrapper;

        KnownSpawnTypes(int w, int min, int max, EntityWrapper<?> entity)
        {
            this.weight = w;
            this.minGroupSize = min;
            this.maxGroupSize = max;
            this.type = entity.type();
            this.wrapper = entity;
        }

        static void applyAll(ResourceLocation biomeID, BiomeModificationContext context)
        {
            for (KnownSpawnTypes value : values()) value.apply(biomeID, context);
        }

        void apply(ResourceLocation biomeID, BiomeModificationContext context)
        {
            if (!wrapper.canSpawn())
            {
                BetterNetherAmbientMobs.LOGGER.debug("Spawn disabled for {} in biome {}", type, biomeID);
                return;
            }

            float multiplier = multiplierFor(biomeID);
            if (multiplier <= 0)
            {
                BetterNetherAmbientMobs.LOGGER.debug("Zero multiplier for {} in biome {}", type, biomeID);
                return;
            }

            int finalWeight = Math.max(1, Math.round(this.weight * multiplier));

            context.getSpawnSettings().addSpawn(
                    MobCategory.AMBIENT,
                    new MobSpawnSettings.SpawnerData(this.type, finalWeight, this.minGroupSize, this.maxGroupSize)
            );

            BetterNetherAmbientMobs.LOGGER.info("Added spawn for {} in biome {} (weight={}, group={}-{})", type, biomeID, finalWeight, minGroupSize, maxGroupSize);
        }

        private float multiplierFor(ResourceLocation biomeID)
        {
            if (this == FIREFLY)
            {
                if (biomeID.equals(Biomes.CRIMSON_FOREST.location())) return 5f;
                if (biomeID.equals(Biomes.WARPED_FOREST.location())) return 5f;
            }

            if (this == HYDROGEN_JELLYFISH)
            {
                if (biomeID.equals(Biomes.NETHER_WASTES.location())) return 5f;
                if (biomeID.equals(Biomes.BASALT_DELTAS.location())) return 5f;
            }

            return 1f;
        }
    }
}
