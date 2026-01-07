package net.silentautopsy.betternetherambientmobs.registry;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier.Builder;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.silentautopsy.betternetherambientmobs.BetterNetherAmbientMobs;
import net.silentautopsy.betternetherambientmobs.entity.custom.EntityFirefly;
import net.silentautopsy.betternetherambientmobs.entity.custom.EntityHydrogenJellyfish;
import net.silentautopsy.betternetherambientmobs.utils.block.BlocksHelper;
import net.silentautopsy.betternetherambientmobs.entity.EntityWrapper;
import net.silentautopsy.betternetherambientmobs.spawn.SpawnRule;
import net.silentautopsy.betternetherambientmobs.spawn.SpawnRuleBuilder;
import net.silentautopsy.betternetherambientmobs.utils.ui.ColorUtil;

public class EntitiesRegistry
{
    public static final EntityWrapper<EntityFirefly> FIREFLY = register(
        "firefly",
        0.5f,
        0.5f,
        EntityFirefly::new,
        EntityFirefly.createMobAttributes(),
        true,
        ColorUtil.color(255, 223, 168),
        ColorUtil.color(233, 182, 95)
    );

    public static final EntityWrapper<EntityHydrogenJellyfish> HYDROGEN_JELLYFISH = register(
        "hydrogen_jellyfish",
        2.0f,
        5.0f,
        EntityHydrogenJellyfish::new,
        EntityHydrogenJellyfish.createMobAttributes(),
        false,
        ColorUtil.color(253, 164, 24),
        ColorUtil.color(88, 21, 4)
    );

    private static <T extends Mob> EntityWrapper<T> register(String name, float width, float height, EntityFactory<T> entity, Builder attributes, boolean fixedSize, int eggColor, int dotsColor)
    {
        ResourceLocation id = BetterNetherAmbientMobs.makeID(name);

        EntityType<T> type = FabricEntityTypeBuilder.create(MobCategory.AMBIENT, entity)
                                .dimensions(fixedSize ? EntityDimensions.fixed(width, height) : EntityDimensions.scalable(width, height))
                                .fireImmune()
                                .build();

        type = Registry.register(BuiltInRegistries.ENTITY_TYPE, id, type);

        FabricDefaultAttributeRegistry.register(type, attributes);

        BetterNetherAmbientMobs.LOGGER.info("Registered entity: {}", name);

        ItemsRegistry.makeEgg("spawn_egg_" + name, type, eggColor, dotsColor);

        return new EntityWrapper<>(type, true);
    }

    public static final int MAX_FLOAT_HEIGHT = 7;

    private static boolean testSpawnAboveLava(LevelAccessor world, BlockPos pos)
    {
        int h = BlocksHelper.downRay(world, pos, MAX_FLOAT_HEIGHT + 2);
        if (h > MAX_FLOAT_HEIGHT) return false;

        for (int i = 1; i <= h + 1; i++)
            if (BlocksHelper.isLava(world.getBlockState(pos.below(i))))
                return false;

        return true;
    }

    public static final SpawnRule<EntityFirefly> RULE_FLOAT_NOT_ABOVE_LAVA = (type, world, spawnReason, pos, random) -> testSpawnAboveLava(world, pos);

    public static void register()
    {
        SpawnRuleBuilder.start(FIREFLY)
                .belowMaxHeight()
                .customRule(RULE_FLOAT_NOT_ABOVE_LAVA)
                .maxNearby(32, 64)
                .buildNoRestrictions(Types.MOTION_BLOCKING_NO_LEAVES);

        SpawnRuleBuilder.start(HYDROGEN_JELLYFISH)
                .belowMaxHeight()
                .maxNearby(32, 64)
                .buildNoRestrictions(Types.MOTION_BLOCKING);
    }
}
