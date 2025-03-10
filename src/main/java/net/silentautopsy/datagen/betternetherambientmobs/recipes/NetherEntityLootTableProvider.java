package net.silentautopsy.datagen.betternetherambientmobs.recipes;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.silentautopsy.betternetherambientmobs.loot.ModLoot;

import java.util.function.BiConsumer;

public class NetherEntityLootTableProvider extends SimpleFabricLootTableProvider
{
    public NetherEntityLootTableProvider(
            FabricDataOutput output
    ) {
        super(output, LootContextParamSets.ENTITY);
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer)
    {
        biConsumer.accept(
                ModLoot.FIREFLY,
                LootTable.lootTable().withPool(killLoot(2, 2, Items.GLOWSTONE_DUST))
        );
    }

    private LootPool.Builder killLoot(int maxDrop, int maxLootingDrop, ItemLike drop)
    {
        return LootPool.lootPool()
                .setRolls(ConstantValue.exactly(1))
                .add(LootItem.lootTableItem(drop)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(0, maxDrop)))
                        .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(1, maxLootingDrop)))
                );
    }
}
