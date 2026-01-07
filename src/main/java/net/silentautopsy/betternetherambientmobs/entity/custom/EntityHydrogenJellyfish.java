package net.silentautopsy.betternetherambientmobs.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.Vec3;
import net.silentautopsy.betternetherambientmobs.entity.DespawnableAnimal;
import net.silentautopsy.betternetherambientmobs.registry.SoundsRegistry;
import org.jetbrains.annotations.NotNull;

public class EntityHydrogenJellyfish extends DespawnableAnimal implements FlyingAnimal
{
    private static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(EntityHydrogenJellyfish.class, EntityDataSerializers.FLOAT);

    private Vec3 preVelocity;
    private Vec3 newVelocity = new Vec3(0, 0, 0);
    private int timer;
    private int timeOut;
    private float prevYaw;
    private float nextYaw;

    public EntityHydrogenJellyfish(EntityType<? extends EntityHydrogenJellyfish> type, Level world)
    {
        super(type, world);
    }

    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        this.entityData.define(SCALE, 0.5F + random.nextFloat());
    }

    public static AttributeSupplier.@NotNull Builder createMobAttributes()
    {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 0.5)
                .add(Attributes.FLYING_SPEED, 0.05)
                .add(Attributes.MOVEMENT_SPEED, 0.5)
                .add(Attributes.ATTACK_DAMAGE, 20.0);
    }

    @Override
    protected boolean isFlapping()
    {
        return true;
    }

    @Override
    protected void jumpInLiquid(@NotNull TagKey<Fluid> fluid)
    {
        this.setDeltaMovement(this.getDeltaMovement().add(0.0D, 0.01D, 0.0D));
    }

    @Override
    public boolean isNoGravity()
    {
        return true;
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag tag)
    {
        super.addAdditionalSaveData(tag);
        tag.putFloat("Scale", getScale());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag tag)
    {
        super.readAdditionalSaveData(tag);

        if (tag.contains("Scale")) this.entityData.set(SCALE, tag.getFloat("Scale"));

        this.refreshDimensions();
    }

    public float getScale()
    {
        return this.entityData.get(SCALE);
    }

    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose)
    {
        return super.getDimensions(pose).scale(this.getScale());
    }

    @Override
    public void playerTouch(Player player)
    {
        player.hurt(player.damageSources().generic(), 3);
    }

    @Override
    public void refreshDimensions()
    {
        double x = this.getX();
        double y = this.getY();
        double z = this.getZ();
        super.refreshDimensions();
        this.setPosRaw(x, y, z);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull EntityDataAccessor<?> data)
    {
        if (SCALE.equals(data)) this.refreshDimensions();
    }

    @Override
    protected void customServerAiStep()
    {
        timer++;
        if (timer > timeOut)
        {
            prevYaw = this.getYRot();
            nextYaw = random.nextFloat() * 360;

            double rads = Math.toRadians(nextYaw + 90);

            double vx = Math.cos(rads) * this.getFlyingSpeed();
            double vz = Math.sin(rads) * this.getFlyingSpeed();

            BlockPos bp = blockPosition();
            double vy = random.nextDouble() * this.getFlyingSpeed() * 0.75;
            if (level().getBlockState(bp).isAir() &&
                    level().getBlockState(bp.below(2)).isAir() &&
                    level().getBlockState(bp.below(3)).isAir() &&
                    level().getBlockState(bp.below(4)).isAir()) {
                vy = -vy;
            }

            preVelocity = newVelocity;
            newVelocity = new Vec3(vx, vy, vz);
            timer = 0;
            timeOut = random.nextInt(300) + 120;
        }

        if (timer <= 120)
        {
            if (this.getYRot() != nextYaw) {
                float delta = timer / 120F;
                this.setYRot(lerpAngleDegrees(delta, prevYaw, nextYaw));
                this.setDeltaMovement(
                        Mth.lerp(delta, preVelocity.x, newVelocity.x),
                        Mth.lerp(delta, preVelocity.y, newVelocity.y),
                        Mth.lerp(delta, preVelocity.z, newVelocity.z)
                );
            }
        }
        else this.setDeltaMovement(newVelocity);
    }

    public static float lerpAngleDegrees(float delta, float first, float second)
    {
        return first + delta * Mth.wrapDegrees(second - first);
    }

    @Override
    public int getMaxSpawnClusterSize()
    {
        return 3;
    }

    @Override
    public void die(@NotNull DamageSource source)
    {
        super.die(source);
        if (level().isClientSide)
        {
            float scale = getScale() * 3;
            for (int i = 0; i < 20; i++)
                this.level().addParticle(ParticleTypes.EXPLOSION,
                        getX() + random.nextGaussian() * scale,
                        getEyeY() + random.nextGaussian() * scale,
                        getZ() + random.nextGaussian() * scale,
                        0, 0, 0
                );
        }
        else if (source != level().damageSources().fellOutOfWorld())
            this.level().explode(this, getX(), getEyeY(), getZ(), 7 * getScale(), Level.ExplosionInteraction.MOB);
    }

    @Override
    public SoundEvent getAmbientSound()
    {
        return SoundsRegistry.MOB_JELLYFISH.value();
    }

    @Override
    protected float getSoundVolume()
    {
        return 0.5F;
    }

    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel world, @NotNull AgeableMob mate)
    {
        return null;
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, @NotNull DamageSource damageSource)
    {
        return false;
    }

    @Override
    protected void checkFallDamage(
            double heightDifference,
            boolean onGround,
            @NotNull BlockState landedState,
            @NotNull BlockPos landedPosition
    )
    {
    }

    @Override
    public boolean hurt(DamageSource source, float amount)
    {
        if (source.is(DamageTypes.WITHER) || source.getDirectEntity() != null || source.is(DamageTypeTags.BYPASSES_INVULNERABILITY))
            return super.hurt(source, amount);
        return false;
    }

    @Override
    public boolean isFlying()
    {
        return !this.onGround();
    }
}
