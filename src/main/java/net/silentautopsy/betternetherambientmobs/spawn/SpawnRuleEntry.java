package net.silentautopsy.betternetherambientmobs.spawn;

import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

public record SpawnRuleEntry<M extends Mob>(byte priority, SpawnRule<M> rule) implements Comparable<SpawnRuleEntry<M>>
{
    @Override
    public int compareTo(@NotNull SpawnRuleEntry<M> entry)
    {
        return Byte.compare(this.priority, entry.priority);
    }
}