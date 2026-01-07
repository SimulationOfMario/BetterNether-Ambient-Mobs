package net.silentautopsy.betternetherambientmobs.spawn;

import com.google.common.collect.Lists;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.silentautopsy.betternetherambientmobs.entity.EntityWrapper;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class SpawnRuleBuilder<M extends Mob>
{
    private final List<SpawnRuleEntry<M>> rules;
    private final EntityType<M> entityType;

    private SpawnRuleBuilder(EntityType<M> entityType)
    {
        this.rules = new ArrayList<>();
        this.entityType = entityType;
    }

    public static <M extends Mob> SpawnRuleBuilder<M> start(EntityWrapper<M> wrapper)
    {
        SpawnRuleBuilder<M> builder = new SpawnRuleBuilder<>(wrapper.type());
        if (!wrapper.canSpawn()) builder.preventSpawn();
        return builder;
    }

    public SpawnRuleBuilder<M> preventSpawn()
    {
        this.rules.add(new SpawnRuleEntry<>((byte) -1, (type, world, spawnReason, pos, random) -> false));
        return this;
    }

    public SpawnRuleBuilder<M> notPeaceful()
    {
        this.rules.add(new SpawnRuleEntry<>((byte) 0, (type, world, spawnReason, pos, random) ->
                world.getDifficulty() != Difficulty.PEACEFUL
        ));
        return this;
    }

    public SpawnRuleBuilder<M> belowMaxHeight()
    {
        this.rules.add(new SpawnRuleEntry<>((byte) 0, (type, world, spawnReason, pos, random) ->
                pos.getY() < world.dimensionType().logicalHeight()
        ));
        return this;
    }

    public SpawnRuleBuilder<M> customRule(SpawnRule<M> rule)
    {
        this.rules.add(new SpawnRuleEntry<>((byte) 7, rule));
        return this;
    }

    public SpawnRuleBuilder<M> maxNearby(EntityType<?> selectorType, int count, int side)
    {
        final Class<? extends Entity> baseClass = selectorType.getBaseClass();
        this.rules.add(new SpawnRuleEntry<>((byte) 3, (type, world, spawnReason, pos, random) -> {
                try
                {
                    final AABB box = new AABB(pos).inflate(side, world.getHeight(), side);
                    final List<?> list = world.getEntitiesOfClass(baseClass, box, (entity) -> true);
                    return list.size() < count;
                }
                catch (Exception e)
                {
                    return true;
                }
            }
        ));
        return this;
    }

    public SpawnRuleBuilder<M> maxNearby(int count, int side)
    {
        return maxNearby(entityType, count, side);
    }

    public void buildNoRestrictions(Heightmap.Types heightmapType)
    {
        build(SpawnPlacements.Type.NO_RESTRICTIONS, heightmapType);
    }

    public void build(SpawnPlacements.Type spawnType, Heightmap.Types heightmapType)
    {
        final List<SpawnRuleEntry<M>> rulesCopy = Lists.newArrayList(this.rules);
        Collections.sort(rulesCopy);

        SpawnPlacements.SpawnPredicate<M> predicate = (entityType, serverLevelAccessor, mobSpawnType, blockPos, random) -> {
            for (SpawnRuleEntry<M> ruleEntry : rulesCopy)
                if (!ruleEntry.rule().canSpawn(entityType, serverLevelAccessor, mobSpawnType, blockPos, random))
                {
                    //BetterNetherAmbientMobs.LOGGER.info("Rejected Spawn of {} at {} {} {} because {} at {} above {}", entityType.getDescriptionId(), blockPos.getX(), blockPos.getY(), blockPos.getZ(), ruleEntry.rule(), serverLevelAccessor.getBlockState(blockPos), serverLevelAccessor.getBlockState(blockPos.below()));
                    return false;
                }

            //BetterNetherAmbientMobs.LOGGER.info("Spawning {} at {} {} {}", entityType.getDescriptionId(), blockPos.getX(), blockPos.getY(), blockPos.getZ());
            return true;
        };

        SpawnPlacements.register(this.entityType, spawnType, heightmapType, predicate);
    }
}