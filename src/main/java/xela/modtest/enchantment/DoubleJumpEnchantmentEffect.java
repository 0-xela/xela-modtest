package xela.modtest.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantedItemInUse;
import net.minecraft.world.item.enchantment.LevelBasedValue;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.phys.Vec3;
import xela.modtest.XelaModtest;

public record DoubleJumpEnchantmentEffect(LevelBasedValue amount) implements EnchantmentEntityEffect {
    public static final MapCodec<DoubleJumpEnchantmentEffect> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            LevelBasedValue.CODEC.fieldOf("amount").forGetter(DoubleJumpEnchantmentEffect::amount)
    ).apply(instance, DoubleJumpEnchantmentEffect::new));

    @Override
    public void apply(ServerLevel serverLevel, int enchantLevel, EnchantedItemInUse enchantedItemInUse, Entity entity, Vec3 vec3) {
        ItemStack item = enchantedItemInUse.itemStack();
        if (item.get(JUMPS) == null || item.get(COOLDOWN) == null) {
            item.set(JUMPS, 0);
            item.set(COOLDOWN, 0);
        } else if (entity.onGround() || entity.isInLiquid()) {
            item.set(JUMPS, enchantLevel);
            item.set(COOLDOWN, 0);
        }

        if (item.get(JUMPS).intValue() > 0
                && item.get(COOLDOWN).intValue() == 0
                && entity instanceof Player
                && entity.isCrouching()
                && !entity.onGround()
                && !entity.isInLiquid()
                && !entity.isSwimming()) { // this will work once i set up the c2s packet instead of using entity.isCrouching()
            XelaModtest.LOGGER.info("double Jumping");
            if (entity instanceof Player player && (player).isFallFlying()) { // If we are flying with an elytra the double jump instead changes all of our momentum to be in the direction we are looking
                Vec3 deltaMovement = entity.getDeltaMovement();
                double magnitude = deltaMovement.length() + 1; // add a little pizzaz
                Vec3 direction = entity.getLookAngle().normalize();
                Vec3 newMovement = direction.scale(magnitude);
                entity.setDeltaMovement(newMovement);
                entity.hurtMarked = true;
                item.set(COOLDOWN, 10);

            } else {
                item.set(JUMPS, enchantedItemInUse.itemStack().get(JUMPS).intValue() - 1);
                Vec3 jumpMovement = new Vec3(entity.getDeltaMovement().x, Math.max(entity.getDeltaMovement().y + .5, .5), entity.getDeltaMovement().z);
                entity.setDeltaMovement(jumpMovement);
                entity.hurtMarked = true;
                item.set(COOLDOWN, 10);
            }
        } else if (entity.isCrouching()) {
            item.set(COOLDOWN, 1);
        } else {
            item.set(COOLDOWN, Math.max(item.get(COOLDOWN).intValue() - 1, 0));
        }
    }

    @Override
    public MapCodec<? extends EnchantmentEntityEffect> codec() {
        return CODEC;
    }

    public static final DataComponentType<Integer> JUMPS = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(XelaModtest.MOD_ID, "double_jump_jumps"),
            DataComponentType.<Integer>builder().persistent(Codec.INT).build()
    );

    public static final DataComponentType<Integer> COOLDOWN = Registry.register(
            BuiltInRegistries.DATA_COMPONENT_TYPE,
            Identifier.fromNamespaceAndPath(XelaModtest.MOD_ID, "double_jump_cooldown"),
            DataComponentType.<Integer>builder().persistent(Codec.INT).build()
    );
}
