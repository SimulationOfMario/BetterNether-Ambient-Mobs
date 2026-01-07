package net.silentautopsy.betternetherambientmobs.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

public record EntityWrapper<T extends Entity>(EntityType<T> type, boolean canSpawn)
{
}
