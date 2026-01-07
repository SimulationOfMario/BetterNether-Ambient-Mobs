package net.silentautopsy.betternetherambientmobs.tag;

import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagEntry;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.TagManager;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.world.item.Item;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

public class TagRegistry<T>
{
    public static class RegistryBacked<T> extends TagRegistry.Simple<T>
    {
        private final DefaultedRegistry<T> registry;

        RegistryBacked(DefaultedRegistry<T> registry)
        {
            super(
                    registry.key(),
                    TagManager.getTagDir(registry.key()),
                    (T element) -> {
                        ResourceLocation id = registry.getKey(element);
                        if (id != registry.getDefaultKey()) {
                            return id;
                        }
                        return null;
                    }
            );
            this.registry = registry;
        }

        @Override
        public TagKey<T> makeTag(ResourceLocation id) {
            final TagKey<T> tag = registry
                    .getTagNames()
                    .filter(tagKey -> tagKey.location().equals(id))
                    .findAny()
                    .orElse(TagKey.create(registry.key(), id));
            initializeTag(tag);
            return tag;
        }
    }

    public static class Simple<T> extends TagRegistry<T>
    {
        Simple(ResourceKey<? extends Registry<T>> registry, String directory, Function<T, ResourceLocation> locationProvider)
        {
            super(registry, directory, locationProvider);
        }

        @SafeVarargs
        public final void add(TagKey<T> tagID, T... elements) {
            super.add(tagID, elements);
        }

        @SafeVarargs
        public final void addOptional(TagKey<T> tagID, T... elements) {
            super.addOptional(tagID, elements);
        }

        @SafeVarargs
        public final void add(T element, TagKey<T>... tagIDs)
        {
            super.add(element, tagIDs);
        }
    }

    public final String directory;
    private final Map<TagKey<T>, Set<TagEntry>> tags = Maps.newConcurrentMap();
    public final ResourceKey<? extends Registry<T>> registryKey;
    private final Function<T, ResourceLocation> locationProvider;

    private TagRegistry(ResourceKey<? extends Registry<T>> registry, String directory, Function<T, ResourceLocation> locationProvider)
    {
        this.registryKey = registry;
        this.directory = directory;
        this.locationProvider = locationProvider;
    }

    protected void initializeTag(TagKey<T> tag)
    {
        getSetForTag(tag);
    }

    public Set<TagEntry> getSetForTag(TagKey<T> tag)
    {
        if (tag == null) return new HashSet<>();

        return tags.computeIfAbsent(tag, k -> Sets.newHashSet());
    }

    public TagKey<T> makeTag(String modId, String name)
    {
        return makeTag(new ResourceLocation(modId, name));
    }

    public TagKey<T> makeTag(ResourceLocation id)
    {
        return creatTagKey(id);
    }

    protected TagKey<T> creatTagKey(ResourceLocation id)
    {
        final TagKey<T> tag = TagKey.create(registryKey, id);
        initializeTag(tag);
        return tag;
    }

    protected void add(TagKey<T> tagID, T... elements)
    {
        add(tagID, false, elements);
    }

    protected void addOptional(TagKey<T> tagID, T... elements)
    {
        add(tagID, true, elements);
    }

    protected void add(TagKey<T> tagID, boolean optional, T... elements)
    {
        Set<TagEntry> set = getSetForTag(tagID);
        for (T element : elements)
        {
            ResourceLocation id = locationProvider.apply(element);
            if (id != null) set.add(optional ? TagEntry.optionalElement(id) : TagEntry.element(id));
        }
    }

    protected void add(T element, TagKey<T>... tagIDs)
    {
        for (TagKey<T> tagID : tagIDs) add(tagID, element);
    }

    public static class Items extends TagRegistry.RegistryBacked<Item>
    {
        Items()
        {
            super(BuiltInRegistries.ITEM);
        }
    }

}

