package net.silentautopsy.betternetherambientmobs.registry;

import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.DispenserBlock;
import net.silentautopsy.betternetherambientmobs.BetterNetherAmbientMobs;
import net.silentautopsy.betternetherambientmobs.tag.TagManager;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.List;

public class ItemsRegistry
{
    private static final List<String> ITEMS_NAMES = new ArrayList<>();
    private static final ArrayList<Item> MOD_ITEMS = new ArrayList<>();

    public static Properties defaultSettings()
    {
        return new Properties();
    }

    @SafeVarargs
    public static void registerItem(String name, Item item, TagKey<Item>... tags)
    {
        if (item != Items.AIR)
        {
            Registry.register(BuiltInRegistries.ITEM, BetterNetherAmbientMobs.makeID(name), item);

            if (tags.length > 0) TagManager.ITEMS.add(item, tags);
            else MOD_ITEMS.add(item);
        }
        if (!(item instanceof BlockItem)) ITEMS_NAMES.add(name);
    }

    public static ArrayList<Item> getModItems()
    {
        return MOD_ITEMS;
    }

    public static List<String> getItemsNames()
    {
        return ITEMS_NAMES;
    }

    public static void makeEgg(String name, EntityType<? extends Mob> type, int background, int dots)
    {
        SpawnEggItem egg = new SpawnEggItem(type, background, dots, defaultSettings());
        DefaultDispenseItemBehavior behavior = new DefaultDispenseItemBehavior()
        {
            public @NotNull ItemStack execute(BlockSource pointer, ItemStack stack)
            {
                Direction direction = pointer.getBlockState().getValue(DispenserBlock.FACING);
                EntityType<?> entityType = ((SpawnEggItem) stack.getItem()).getType(stack.getTag());
                entityType.spawn(
                        pointer.getLevel(),
                        stack,
                        null,
                        pointer.getPos().relative(direction),
                        MobSpawnType.DISPENSER,
                        direction != Direction.UP,
                        false
                );
                stack.shrink(1);
                return stack;
            }
        };
        DispenserBlock.registerBehavior(egg, behavior);
        registerItem(name, egg);
    }

}
