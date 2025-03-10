package net.silentautopsy.betternetherambientmobs.tab;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.silentautopsy.betternetherambientmobs.registry.NetherItems;

import java.util.List;

public class CreativeTabs {
    public static void register()
    {
        List<Item> items = NetherItems.getModItems();

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.SPAWN_EGGS).register(content -> {
            content.accept(items.get(0));
            content.accept(items.get(1));
        });
    }
}
