package net.silentautopsy.betternetherambientmobs.tag;

import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import com.google.common.collect.Maps;

import java.util.Map;

public class TagManager
{
    private static final Map<String, TagRegistry<?>> TYPES = Maps.newHashMap();

    public static TagRegistry.Items ITEMS = registerItem();
    public static TagRegistry.RegistryBacked<Block> BLOCKS = registerType(BuiltInRegistries.BLOCK);

    public static <T> TagRegistry.RegistryBacked<T> registerType(DefaultedRegistry<T> registry)
    {
        TagRegistry.RegistryBacked<T> type = new TagRegistry.RegistryBacked<>(registry);
        return (TagRegistry.RegistryBacked<T>) TYPES.computeIfAbsent(type.directory, dir -> type);
    }

    public static TagRegistry.Items registerItem()
    {
        TagRegistry.Items type = new TagRegistry.Items();
        return (TagRegistry.Items) TYPES.computeIfAbsent(type.directory, (dir) -> type);
    }
}