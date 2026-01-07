package net.silentautopsy.betternetherambientmobs.utils.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.LavaFluid;

public class BlocksHelper
{
    public static int downRay(LevelAccessor world, BlockPos pos, int maxDist)
    {
        int length = 0;
        for (int j = 1; j < maxDist && (world.isEmptyBlock(pos.below(j))); j++) length++;
        return length;
    }

    public static boolean isLava(BlockState state)
    {
        return state.getFluidState().getType() instanceof LavaFluid;
    }
}

