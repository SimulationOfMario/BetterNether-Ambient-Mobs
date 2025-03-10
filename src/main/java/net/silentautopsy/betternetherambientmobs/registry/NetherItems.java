package net.silentautopsy.betternetherambientmobs.registry;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Direction;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.level.block.DispenserBlock;
import net.silentautopsy.betternetherambientmobs.BetterNetherAmbientMobs;
import org.betterx.bclib.config.PathConfig;
import org.betterx.bclib.registry.ItemRegistry;
import org.betterx.worlds.together.tag.v3.TagManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class NetherItems extends ItemRegistry
{

    public NetherItems()
    {
        super(new PathConfig(BetterNetherAmbientMobs.MOD_ID, "items"));
    }

    public static Properties defaultSettings()
    {
        return new Properties();
    }


    public static Item makeEgg(String name, EntityType<? extends Mob> type, int background, int dots) {

            SpawnEggItem egg = new SpawnEggItem(type, background, dots, defaultSettings());
            DefaultDispenseItemBehavior behavior = new DefaultDispenseItemBehavior() {
                public ItemStack execute(BlockSource pointer, ItemStack stack) {
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
            NetherItems.registerItem(name, egg);
            return egg;
    }

    public static Item registerItem(String name, Item item, TagKey<Item>... tags) {
        if (item != Items.AIR)
        {
            getItemRegistry().register(BetterNetherAmbientMobs.makeID(name), item);
            //item = Registry.register(Registry.ITEM, new ResourceLocation(BetterNether.MOD_ID, name), item);

            if (tags.length > 0)
                TagManager.ITEMS.add(item, tags);

            if (item instanceof BlockItem)
                MOD_BLOCKS.add(item);
            else
                MOD_ITEMS.add(item);
        }
        if (!(item instanceof BlockItem))
            ITEMS.add(name);
        return item;
    }

    private static final List<String> ITEMS = new ArrayList<String>();
    private static final ArrayList<Item> MOD_BLOCKS = new ArrayList<Item>();
    private static final ArrayList<Item> MOD_ITEMS = new ArrayList<Item>();
    private static ItemRegistry ITEMS_REGISTRY;

    public static ArrayList<Item> getModItems() {
        return MOD_ITEMS;
    }

    @NotNull
    public static ItemRegistry getItemRegistry() {
        if (ITEMS_REGISTRY == null) {
            ITEMS_REGISTRY = new NetherItems();
        }
        return ITEMS_REGISTRY;
    }

}
