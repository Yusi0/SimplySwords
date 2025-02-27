package net.sweenus.simplyswords.entity;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.sweenus.simplyswords.config.SimplySwordsConfig;
import net.sweenus.simplyswords.registry.EffectRegistry;
import net.sweenus.simplyswords.registry.SoundRegistry;
import net.sweenus.simplyswords.util.HelperMethods;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class BattleStandardEntity extends PathAwareEntity {
    public static final Supplier<EntityType<BattleStandardEntity>> TYPE = Suppliers.memoize(() -> EntityType.Builder.create(BattleStandardEntity::new, SpawnGroup.MISC).build("battlestandard"));
    int abilityDamage =  (int) (SimplySwordsConfig.getFloatValue("abyssalstandard_damage"));
    public PlayerEntity ownerEntity;
    public String standardType;
    public int decayRate;

    public static DefaultAttributeContainer.Builder createBattleStandardAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 150.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.0f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 100.0f);
    }

    public BattleStandardEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected boolean isImmobile() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }
    @Override
    public boolean damage(DamageSource source, float amount) {
        return ownerEntity == null;
    }

    @Override
    public void baseTick() {

        if (!this.world.isClient()) {
            if (this.age % 10 == 0) {
                this.setHealth(this.getHealth() - decayRate);
                if (ownerEntity == null)
                    this.setHealth(this.getHealth() - 1000);
            }
            if (ownerEntity != null && standardType != null) {
                int radius = 6;
                int abilityDamage = 2;
                //AOE Aura
                if (this.age % 10 == 0) {
                    Box box = new Box(this.getX() + radius, this.getY() + (float) radius / 3, this.getZ() + radius, this.getX() - radius, this.getY() - (float) radius / 3, this.getZ() - radius);
                    for (Entity entities : world.getOtherEntities(this, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                        if (entities != null) {
                            if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, ownerEntity)
                                    && le != ownerEntity && !(le instanceof BattleStandardEntity)
                                    && !(le instanceof BattleStandardDarkEntity)) {

                                //Sunfire negative effects
                                if (Objects.equals(standardType, "sunfire")) {
                                    le.damage(DamageSource.MAGIC, abilityDamage);
                                    le.setOnFireFor(1);
                                    le.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, 120, 1), this);
                                }
                                //Nullification negative effects
                                if (Objects.equals(standardType, "nullification")) {
                                    for (StatusEffectInstance statusEffect : le.getStatusEffects()) {
                                        if (statusEffect != null && statusEffect.getEffectType().isBeneficial()) {
                                            le.removeStatusEffect(statusEffect.getEffectType());
                                            break;
                                        }
                                    }
                                }

                            }
                        }
                    }
                    HelperMethods.spawnParticle(world, ParticleTypes.LAVA, this.getX(),
                            this.getY(),
                            this.getZ(),
                            0, 0, 0);
                }

                //Landing effects
                if (this.getHealth() > this.getMaxHealth() - 2 && this.isOnGround()) {

                    HelperMethods.spawnParticle(world, ParticleTypes.LAVA, this.getX(),
                            this.getY(),
                            this.getZ(),
                            0, 0.3, 0);
                    HelperMethods.spawnParticle(world, ParticleTypes.CAMPFIRE_COSY_SMOKE, this.getX(),
                            this.getY(),
                            this.getZ(),
                            0, 0.1, 0);

                    //Launch nearby entities on land
                    Box box = new Box(this.getX() + 1, this.getY() + 1, this.getZ() + 1, this.getX()
                            - 1, this.getY() - (float) 1, this.getZ() - 1);
                    for (Entity entities : world.getOtherEntities(this, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                        if (entities != null) {
                            if ((entities instanceof LivingEntity le) && HelperMethods.checkFriendlyFire(le, ownerEntity) && le != ownerEntity) {
                                le.damage(DamageSource.MAGIC, abilityDamage * 3);
                                le.setOnFireFor(1);
                                le.setVelocity((le.getX() - this.getX()) / 4, 0.5, (le.getZ() - this.getZ()) / 4);
                            }
                        }
                    }


                }


                if (this.age % 80 == 0) {

                    Box box = new Box(this.getX() + radius, this.getY() + (float) radius / 3, this.getZ() + radius, this.getX() - radius, this.getY() - (float) radius / 3, this.getZ() - radius);
                    for (Entity entities : world.getOtherEntities(this, box, EntityPredicates.VALID_LIVING_ENTITY)) {

                        if (entities != null) {
                            if ((entities instanceof LivingEntity le) && !HelperMethods.checkFriendlyFire(le, ownerEntity)) {
                                //Sunfire positive effects
                                if (Objects.equals(standardType, "sunfire")) {
                                    le.heal(3);
                                    le.addStatusEffect(new StatusEffectInstance(StatusEffects.STRENGTH, 90, 1), this);
                                }

                                //Nullification positive effects
                                if (Objects.equals(standardType, "nullification")) {
                                    for (StatusEffectInstance statusEffect : le.getStatusEffects()) {
                                        if (statusEffect != null && !statusEffect.getEffectType().isBeneficial() && !Objects.equals(statusEffect.getEffectType(), EffectRegistry.BATTLE_FATIGUE.get())) {
                                            le.removeStatusEffect(statusEffect.getEffectType());
                                            break;
                                        }
                                    }
                                }

                            }
                        }
                    }

                    world.playSoundFromEntity(null, this, SoundRegistry.ELEMENTAL_BOW_EARTH_SHOOT_IMPACT_02.get(), SoundCategory.PLAYERS, 0.1f, 0.6f);
                    double xpos = this.getX() - (radius + 1);
                    double ypos = this.getY();
                    double zpos = this.getZ() - (radius + 1);

                    for (int i = radius * 2; i > 0; i--) {
                        for (int j = radius * 2; j > 0; j--) {
                            float choose = (float) (Math.random() * 1);
                            if (choose > 0.5)
                                HelperMethods.spawnParticle(world, ParticleTypes.CAMPFIRE_COSY_SMOKE, xpos + i + choose,
                                        ypos,
                                        zpos + j + choose,
                                        0, -0.1, 0);
                        }
                    }
                }
            }
        }



        super.baseTick();
    }


}
