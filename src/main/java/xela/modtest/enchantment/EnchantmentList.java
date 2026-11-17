package xela.modtest.enchantment;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import xela.modtest.XelaModtest;

public class EnchantmentList {
    public static MapCodec<DoubleJumpEnchantmentEffect> DOUBLE_JUMP_ENCHANTMENT = register("double_jump", DoubleJumpEnchantmentEffect.CODEC);

    private static <T extends EnchantmentEntityEffect> MapCodec<T> register(String id, MapCodec<T> codec) {
        return Registry.register(BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, Identifier.fromNamespaceAndPath(XelaModtest.MOD_ID, id), codec);
    }

    public static void initialize() {
        XelaModtest.LOGGER.info("Initializing Enchantments");
    }
}
