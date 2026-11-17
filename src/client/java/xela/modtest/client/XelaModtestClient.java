package xela.modtest.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import xela.modtest.XelaModtest;
import xela.modtest.client.net.DoubleJumpC2SPayload;

public class XelaModtestClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		// This entrypoint is suitable for setting up client-specific logic, such as rendering.
		KeyMapping.Category CATEGORY = KeyMapping.Category.register(
				Identifier.fromNamespaceAndPath(XelaModtest.MOD_ID, "xelamod-binds")
		);
		KeyMapping DoubleJumpKey = KeyBindingHelper.registerKeyBinding(
				new KeyMapping(
						"key.xela-modtest.doublejump",
						InputConstants.Type.KEYSYM,
						InputConstants.KEY_J,
						CATEGORY
				));
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			while (DoubleJumpKey.consumeClick()) {
				if (client.player != null) {
					DoubleJumpC2SPayload payload = new DoubleJumpC2SPayload(client.player.getId());
					ClientPlayNetworking.send(payload);
				}
			}
		});
		PayloadTypeRegistry.playC2S().register(DoubleJumpC2SPayload.ID, DoubleJumpC2SPayload.CODEC);
	}
}