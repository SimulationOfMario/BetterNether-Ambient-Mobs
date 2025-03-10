package net.silentautopsy.betternetherambientmobs.mixin.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.DeltaFeature;
import net.minecraft.world.level.levelgen.feature.configurations.DeltaFeatureConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DeltaFeature.class)
public class DeltaFeatureMixin {
    @Inject(method = "isClear", at = @At("HEAD"), cancellable = true)
    private static void checkBlock(
            LevelAccessor worldAccess,
            BlockPos blockPos,
            DeltaFeatureConfiguration deltaFeatureConfig,
            CallbackInfoReturnable<Boolean> info
    ) {
        BlockState blockState = worldAccess.getBlockState(blockPos);
        if (!bn_isValidBlock(blockState)) {
            info.setReturnValue(false);
            info.cancel();
        }
    }

    private static boolean bn_isValidBlock(BlockState state) {
        return !(state.getFluidState().isEmpty()
                && state.canBeReplaced());
    }
}
