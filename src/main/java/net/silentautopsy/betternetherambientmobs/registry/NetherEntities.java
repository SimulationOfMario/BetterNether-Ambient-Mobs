package net.silentautopsy.betternetherambientmobs.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.silentautopsy.betternetherambientmobs.BetterNetherAmbientMobs;
import net.silentautopsy.betternetherambientmobs.entity.custom.EntityFirefly;
import net.silentautopsy.betternetherambientmobs.entity.custom.EntityHydrogenJellyfish;
import org.betterx.bclib.api.v2.levelgen.biomes.BiomeAPI;
import org.betterx.bclib.api.v2.spawning.SpawnRuleBuilder;
import org.betterx.bclib.entity.BCLEntityWrapper;
import org.betterx.bclib.interfaces.SpawnRule;
import org.betterx.ui.ColorUtil;


public class NetherEntities
{
    public enum KnownSpawnTypes
    {
        FIREFLY(5, 1, 5, NetherEntities.FIREFLY),
        HYDROGEN_JELLYFISH(5, 2, 6, NetherEntities.HYDROGEN_JELLYFISH);

        public final int weight;
        public final int minGroupSize;
        public final int maxGroupSize;
        public final EntityType type;
        public final BCLEntityWrapper wrapper;

        public void addSpawn(ResourceLocation ID, Holder<Biome> biome, float multiplier)
        {
            BetterNetherAmbientMobs.LOGGER.info("Adding spawn for " + this.type + " in biome " + ID + " with weight " + (int) (weight * multiplier) + " and group size " + minGroupSize + " - " + maxGroupSize);

            BiomeAPI.addBiomeMobSpawn(biome, this.type, (int) (this.weight * multiplier), this.minGroupSize, this.maxGroupSize);
        }

        <M extends Mob> KnownSpawnTypes(int w, int min, int max, BCLEntityWrapper type)
        {
            weight = w;
            minGroupSize = min;
            maxGroupSize = max;
            this.type = type.type();
            this.wrapper = type;
        }
    }

    public static final BCLEntityWrapper<EntityFirefly> FIREFLY =
            register(
                    "firefly",
                    MobCategory.AMBIENT,
                    0.5f,
                    0.5f,
                    EntityFirefly::new,
                    EntityFirefly.createMobAttributes(),
                    true,
                    ColorUtil.color(255, 223, 168),
                    ColorUtil.color(233, 182, 95)
            );

    public static final BCLEntityWrapper<EntityHydrogenJellyfish> HYDROGEN_JELLYFISH =
            register(
                    "hydrogen_jellyfish",
                    MobCategory.AMBIENT,
                    2.0f,
                    5.0f,
                    EntityHydrogenJellyfish::new,
                    EntityHydrogenJellyfish.createMobAttributes(),
                    false,
                    ColorUtil.color(253, 164, 24),
                    ColorUtil.color(88, 21, 4)
            );

    private static <T extends Mob> BCLEntityWrapper<T> register(
            String name,
            MobCategory group,
            float width,
            float height,
            EntityFactory<T> entity,
            Builder attributes,
            boolean fixedSize,
            int eggColor,
            int dotsColor
    )
    {
        ResourceLocation id = BetterNetherAmbientMobs.makeID(name);
        EntityType<T> type = FabricEntityTypeBuilder.create(group, entity)
                                                    .dimensions(fixedSize ? EntityDimensions.fixed(width, height) : EntityDimensions.scalable(width, height))
                                                    .fireImmune() //Nether Entities are by default immune to fire
                                                    .build();

        type = Registry.register(BuiltInRegistries.ENTITY_TYPE, id, type);

        FabricDefaultAttributeRegistry.register(type, attributes);

        BetterNetherAmbientMobs.LOGGER.info("Registered entity: " + name);

        NetherItems.makeEgg("spawn_egg_" + name, type, eggColor, dotsColor);

        var wrapper = new BCLEntityWrapper<>(type, true);
        return wrapper;
    }

    private static boolean testSpawnAboveLava(LevelAccessor world, BlockPos pos, boolean allow)
    {
        int h = org.betterx.bclib.util.BlocksHelper.downRay(world, pos, MAX_FLOAT_HEIGHT + 2);
        if (h > MAX_FLOAT_HEIGHT) return false;

        for (int i = 1; i <= h + 1; i++)
            if (org.betterx.bclib.util.BlocksHelper.isLava(world.getBlockState(pos.below(i))))
                return allow;

        return !allow;
    }

    public static final int MAX_FLOAT_HEIGHT = 7;
    public static final SpawnRule RULE_FLOAT_NOT_ABOVE_LAVA = (type, world, spawnReason, pos, random) -> testSpawnAboveLava(world, pos, false);


    public static void register()
    {

        SpawnRuleBuilder
                .start(FIREFLY)
                .belowMaxHeight()
                .customRule(RULE_FLOAT_NOT_ABOVE_LAVA)
                .maxNearby(32, 64)
                .buildNoRestrictions(Types.MOTION_BLOCKING_NO_LEAVES);

        SpawnRuleBuilder
                .start(HYDROGEN_JELLYFISH)
                .belowMaxHeight()
                .maxNearby(24, 64)
                .buildNoRestrictions(Types.MOTION_BLOCKING);
    }


    public static void modifyNonBNBiome(ResourceLocation biomeID, Holder<Biome> biome)
    {
        final boolean isCrimson = biomeID.equals(Biomes.CRIMSON_FOREST.location());
        final boolean isBasalt = biomeID.equals(Biomes.BASALT_DELTAS.location());
        final boolean isWastes = biomeID.equals(Biomes.NETHER_WASTES.location());

        KnownSpawnTypes.FIREFLY.addSpawn(biomeID, biome, isCrimson ? 5 : 1);
        KnownSpawnTypes.HYDROGEN_JELLYFISH.addSpawn(biomeID, biome, isWastes || isBasalt ? 3 : 1);
    }
}
