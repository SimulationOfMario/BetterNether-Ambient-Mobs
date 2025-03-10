package net.silentautopsy.datagen.betternetherambientmobs;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.silentautopsy.datagen.betternetherambientmobs.recipes.NetherEntityLootTableProvider;

public class BetterNetherAmbientMobsDatagen implements DataGeneratorEntrypoint
{
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator)
    {
        final FabricDataGenerator.Pack pack = dataGenerator.createPack();

        pack.addProvider(NetherEntityLootTableProvider::new);
    }
}
