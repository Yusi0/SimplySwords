package net.sweenus.simplyswords.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterial;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.ClickType;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.sweenus.simplyswords.SimplySwords;
import net.sweenus.simplyswords.config.SimplySwordsConfig;
import net.sweenus.simplyswords.registry.EffectRegistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RunicSwordItem extends SwordItem {
    public RunicSwordItem(ToolMaterial toolMaterial, int attackDamage, float attackSpeed, Settings settings) {
        super(toolMaterial, attackDamage, attackSpeed, settings);
    }

    @Override
    public boolean onClicked(ItemStack stack, ItemStack otherStack, Slot slot, ClickType clickType, PlayerEntity player, StackReference cursorStackReference) {

        if(stack.getOrCreateNbt().getInt("runic_power") == 0) {

            int choose = (int) (Math.random() * 100);
            stack.getOrCreateNbt().putInt("runic_power", choose);
        }

        return false;
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {

        //FREEZE
        if(stack.getOrCreateNbt().getInt("runic_power") <= 10 && stack.getOrCreateNbt().getInt("runic_power") >= 1) {

            int fhitchance = (int) SimplySwordsConfig.getFloatValue("freeze_chance");
            int fduration = (int) SimplySwordsConfig.getFloatValue("freeze_duration");
            int sduration = (int) SimplySwordsConfig.getFloatValue("slowness_duration");

            target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, sduration, 1), attacker);

            if (attacker.getRandom().nextInt(100) <= fhitchance) {
                target.addStatusEffect(new StatusEffectInstance(EffectRegistry.FREEZE.get(), fduration, 1), attacker);
            }
        }
        //WILDFIRE
        if(stack.getOrCreateNbt().getInt("runic_power") > 10 && stack.getOrCreateNbt().getInt("runic_power") <= 20) {
            int phitchance = (int) SimplySwordsConfig.getFloatValue("wildfire_chance");
            int pduration = (int) SimplySwordsConfig.getFloatValue("wildfire_duration");

            if (attacker.getRandom().nextInt(100) <= phitchance) {
                target.addStatusEffect(new StatusEffectInstance(EffectRegistry.WILDFIRE.get(), pduration, 3), attacker);
            }
        }
        //SLOW
        if(stack.getOrCreateNbt().getInt("runic_power") > 20 && stack.getOrCreateNbt().getInt("runic_power") <= 30) {
            int shitchance = (int) SimplySwordsConfig.getFloatValue("slowness_chance");
            int sduration = (int) SimplySwordsConfig.getFloatValue("slowness_duration");

            if (attacker.getRandom().nextInt(100) <= shitchance) {
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, sduration, 3), attacker);
            }
        }
        //SPEED
        if(stack.getOrCreateNbt().getInt("runic_power") > 30 && stack.getOrCreateNbt().getInt("runic_power") <= 40) {
            int shitchance = (int) SimplySwordsConfig.getFloatValue("speed_chance");
            int sduration = (int) SimplySwordsConfig.getFloatValue("speed_duration");

            if (attacker.getRandom().nextInt(100) <= shitchance) {
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, sduration, 1), attacker);
            }
        }
        //LEVITATION
        if(stack.getOrCreateNbt().getInt("runic_power") > 40 && stack.getOrCreateNbt().getInt("runic_power") <= 50) {
            int lhitchance = (int) SimplySwordsConfig.getFloatValue("levitation_chance");
            int lduration = (int) SimplySwordsConfig.getFloatValue("levitation_duration");

            if (attacker.getRandom().nextInt(100) <= lhitchance) {
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.LEVITATION, lduration, 3), attacker);
            }
        }
        //ZEPHYR
        if(stack.getOrCreateNbt().getInt("runic_power") > 50 && stack.getOrCreateNbt().getInt("runic_power") <= 60) {
            int lhitchance = (int) SimplySwordsConfig.getFloatValue("zephyr_chance");
            int lduration = (int) SimplySwordsConfig.getFloatValue("zephyr_duration");

            if (attacker.getRandom().nextInt(100) <= lhitchance) {
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.HASTE, lduration, 3), attacker);
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, lduration, 1), attacker);
            }
        }
        //SHIELDING
        if(stack.getOrCreateNbt().getInt("runic_power") > 60 && stack.getOrCreateNbt().getInt("runic_power") <= 70) {
            int lhitchance = (int) SimplySwordsConfig.getFloatValue("shielding_chance");
            int lduration = (int) SimplySwordsConfig.getFloatValue("shielding_duration");

            if (attacker.getRandom().nextInt(100) <= lhitchance) {
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, lduration, 1), attacker);
            }
        }
        //STONESKIN
        if(stack.getOrCreateNbt().getInt("runic_power") > 70 && stack.getOrCreateNbt().getInt("runic_power") <= 80) {
            int lhitchance = (int) SimplySwordsConfig.getFloatValue("stoneskin_chance");
            int lduration = (int) SimplySwordsConfig.getFloatValue("stoneskin_duration");

            if (attacker.getRandom().nextInt(100) <= lhitchance) {
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, lduration, 2), attacker);
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, lduration, 1), attacker);
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, lduration, 1), attacker);
            }
        }
        //TRAILBLAZE
        if(stack.getOrCreateNbt().getInt("runic_power") > 80 && stack.getOrCreateNbt().getInt("runic_power") <= 90) {
            int lhitchance = (int) SimplySwordsConfig.getFloatValue("trailblaze_chance");
            int lduration = (int) SimplySwordsConfig.getFloatValue("trailblaze_duration");

            if (attacker.getRandom().nextInt(100) <= lhitchance) {
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.SPEED, lduration, 3), attacker);
                attacker.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, lduration, 1), attacker);
                attacker.setOnFireFor(lduration / 20);
            }
        }
        //WEAKEN
        if(stack.getOrCreateNbt().getInt("runic_power") > 90 && stack.getOrCreateNbt().getInt("runic_power") <= 100) {
            int lhitchance = (int) SimplySwordsConfig.getFloatValue("weaken_chance");
            int lduration = (int) SimplySwordsConfig.getFloatValue("weaken_duration");

            if (attacker.getRandom().nextInt(100) <= lhitchance) {
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.WEAKNESS, lduration, 1), attacker);
                target.addStatusEffect(new StatusEffectInstance(StatusEffects.SLOWNESS, lduration, 1), attacker);
            }
        }

        return super.postHit(stack, target, attacker);

    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player)
    {
        if(world.isClient) return;

            int choose = (int) (Math.random() * 100);
            stack.getOrCreateNbt().putInt("runic_power", choose);
    }

    @Override
    public void appendTooltip(ItemStack itemStack, World world, List<Text> tooltip, TooltipContext tooltipContext) {

        if(itemStack.getOrCreateNbt().getInt("runic_power") == 0) {

            tooltip.add(Text.literal(""));
            tooltip.add(Text.translatable("item.simplyswords.unidentifiedsworditem.tooltip1").formatted(Formatting.AQUA, Formatting.BOLD));
            tooltip.add(Text.translatable("item.simplyswords.unidentifiedsworditem.tooltip2"));

        }
        if(itemStack.getOrCreateNbt().getInt("runic_power") <= 10 && itemStack.getOrCreateNbt().getInt("runic_power") >= 1) {

            tooltip.add(Text.literal(""));
            tooltip.add(Text.translatable("item.simplyswords.freezesworditem.tooltip1").formatted(Formatting.AQUA, Formatting.BOLD));
            tooltip.add(Text.translatable("item.simplyswords.freezesworditem.tooltip2"));

        }
        if(itemStack.getOrCreateNbt().getInt("runic_power") > 10 && itemStack.getOrCreateNbt().getInt("runic_power") <= 20) {

            tooltip.add(Text.literal(""));
            tooltip.add(Text.translatable("item.simplyswords.wildfiresworditem.tooltip1").formatted(Formatting.AQUA, Formatting.BOLD));
            tooltip.add(Text.translatable("item.simplyswords.wildfiresworditem.tooltip2"));
            tooltip.add(Text.translatable("item.simplyswords.wildfiresworditem.tooltip3"));

        }
        if(itemStack.getOrCreateNbt().getInt("runic_power") > 20 && itemStack.getOrCreateNbt().getInt("runic_power") <= 30) {

            tooltip.add(Text.literal(""));
            tooltip.add(Text.translatable("item.simplyswords.slownesssworditem.tooltip1").formatted(Formatting.AQUA, Formatting.BOLD));
            tooltip.add(Text.translatable("item.simplyswords.slownesssworditem.tooltip2"));
            tooltip.add(Text.translatable("item.simplyswords.slownesssworditem.tooltip3"));

        }
        if(itemStack.getOrCreateNbt().getInt("runic_power") > 30 && itemStack.getOrCreateNbt().getInt("runic_power") <= 40) {

            tooltip.add(Text.literal(""));
            tooltip.add(Text.translatable("item.simplyswords.speedsworditem.tooltip1").formatted(Formatting.AQUA, Formatting.BOLD));
            tooltip.add(Text.translatable("item.simplyswords.speedsworditem.tooltip2"));
            tooltip.add(Text.translatable("item.simplyswords.speedsworditem.tooltip3"));

        }
        if(itemStack.getOrCreateNbt().getInt("runic_power") > 40 && itemStack.getOrCreateNbt().getInt("runic_power") <= 50) {

            tooltip.add(Text.literal(""));
            tooltip.add(Text.translatable("item.simplyswords.levitationsworditem.tooltip1").formatted(Formatting.AQUA, Formatting.BOLD));
            tooltip.add(Text.translatable("item.simplyswords.levitationsworditem.tooltip2"));
            tooltip.add(Text.translatable("item.simplyswords.levitationsworditem.tooltip3"));

        }
        if(itemStack.getOrCreateNbt().getInt("runic_power") > 50 && itemStack.getOrCreateNbt().getInt("runic_power") <= 60) {

            tooltip.add(Text.literal(""));
            tooltip.add(Text.translatable("item.simplyswords.zephyrsworditem.tooltip1").formatted(Formatting.AQUA, Formatting.BOLD));
            tooltip.add(Text.translatable("item.simplyswords.zephyrsworditem.tooltip2"));
            tooltip.add(Text.translatable("item.simplyswords.zephyrsworditem.tooltip3"));

        }
        if(itemStack.getOrCreateNbt().getInt("runic_power") > 60 && itemStack.getOrCreateNbt().getInt("runic_power") <= 70) {

            tooltip.add(Text.literal(""));
            tooltip.add(Text.translatable("item.simplyswords.shieldingsworditem.tooltip1").formatted(Formatting.AQUA, Formatting.BOLD));
            tooltip.add(Text.translatable("item.simplyswords.shieldingsworditem.tooltip2"));
            tooltip.add(Text.translatable("item.simplyswords.shieldingsworditem.tooltip3"));

        }
        if(itemStack.getOrCreateNbt().getInt("runic_power") > 70 && itemStack.getOrCreateNbt().getInt("runic_power") <= 80) {

            tooltip.add(Text.literal(""));
            tooltip.add(Text.translatable("item.simplyswords.stoneskinsworditem.tooltip1").formatted(Formatting.AQUA, Formatting.BOLD));
            tooltip.add(Text.translatable("item.simplyswords.stoneskinsworditem.tooltip2"));
            tooltip.add(Text.translatable("item.simplyswords.stoneskinsworditem.tooltip3"));

        }
        if(itemStack.getOrCreateNbt().getInt("runic_power") > 80 && itemStack.getOrCreateNbt().getInt("runic_power") <= 90) {

            tooltip.add(Text.literal(""));
            tooltip.add(Text.translatable("item.simplyswords.trailblazesworditem.tooltip1").formatted(Formatting.AQUA, Formatting.BOLD));
            tooltip.add(Text.translatable("item.simplyswords.trailblazesworditem.tooltip2"));
            tooltip.add(Text.translatable("item.simplyswords.trailblazesworditem.tooltip3"));

        }
        if(itemStack.getOrCreateNbt().getInt("runic_power") > 90 && itemStack.getOrCreateNbt().getInt("runic_power") <= 100) {

            tooltip.add(Text.literal(""));
            tooltip.add(Text.translatable("item.simplyswords.weakensworditem.tooltip1").formatted(Formatting.AQUA, Formatting.BOLD));
            tooltip.add(Text.translatable("item.simplyswords.weakensworditem.tooltip2"));
            tooltip.add(Text.translatable("item.simplyswords.weakensworditem.tooltip3"));

        }

    }
}
