package net.silentautopsy.betternetherambientmobs.registry;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.silentautopsy.betternetherambientmobs.BetterNetherAmbientMobs;

public class SoundsRegistry
{
    public static final Holder<SoundEvent> MOB_FIREFLY_FLY = registerHolder("betternetherambientmobs.mob.firefly.fly");
    public static final Holder<SoundEvent> MOB_JELLYFISH = registerHolder("betternetherambientmobs.mob.jellyfish");

    public static SoundEvent register(String id)
    {
        ResourceLocation loc = new ResourceLocation(BetterNetherAmbientMobs.MOD_ID, id);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, loc, SoundEvent.createVariableRangeEvent(loc));
    }

    public static Holder<SoundEvent> registerHolder(String id)
    {
        ResourceLocation loc = new ResourceLocation(BetterNetherAmbientMobs.MOD_ID, id);
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT, loc, SoundEvent.createVariableRangeEvent(loc));
    }

    public static void ensureStaticallyLoaded()
    {
    }
}
