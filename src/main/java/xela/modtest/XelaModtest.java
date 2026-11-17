package xela.modtest;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xela.modtest.enchantment.DoubleJumpC2SPayload;
import xela.modtest.enchantment.EnchantmentList;
import xela.modtest.item.ItemList;

public class XelaModtest implements ModInitializer {
	public static final String MOD_ID = "xela-modtest";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("ModTest Loaded");
		ItemList.initialize();
		PayloadTypeRegistry.playC2S().register(DoubleJumpC2SPayload.ID, DoubleJumpC2SPayload.CODEC);
		ServerPlayNetworking.registerGlobalReceiver(DoubleJumpC2SPayload.ID, (payload, context) -> {
			Entity entity = context.player().level().getEntity(payload.entityID());
			entity.addTag("wants-to-jump");
			LOGGER.info("yapa:");
		});
		EnchantmentList.initialize();


	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
