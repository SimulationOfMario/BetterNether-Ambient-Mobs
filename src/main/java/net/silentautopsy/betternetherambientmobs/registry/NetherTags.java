package net.silentautopsy.betternetherambientmobs.registry;

import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.silentautopsy.betternetherambientmobs.BetterNetherAmbientMobs;
import org.betterx.worlds.together.tag.v3.TagManager;

public class NetherTags
{

    public static final TagKey<Block> FIREFLY_FLOWERS = TagManager.BLOCKS.makeTag(
            BetterNetherAmbientMobs.MOD_ID,
            "firefly_flowers"
    );

    public static void register()
    {
        TagManager.BLOCKS.add(FIREFLY_FLOWERS,
                Blocks.NETHER_WART,
                Blocks.NETHER_WART_BLOCK,
                Blocks.CRIMSON_ROOTS,
                Blocks.WARPED_ROOTS,
                Blocks.WARPED_WART_BLOCK
        );
    }
}