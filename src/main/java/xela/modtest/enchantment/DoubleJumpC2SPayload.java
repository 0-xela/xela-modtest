package xela.modtest.enchantment;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import xela.modtest.XelaModtest;

public record DoubleJumpC2SPayload(int entityID) implements CustomPacketPayload {
    public static final Identifier DOUBLEJUMP_PAYLOAD_ID = Identifier.fromNamespaceAndPath(XelaModtest.MOD_ID, "double_jump_ask");
    public static final CustomPacketPayload.Type<DoubleJumpC2SPayload> ID = new CustomPacketPayload.Type<>(DOUBLEJUMP_PAYLOAD_ID);
    public static final StreamCodec<RegistryFriendlyByteBuf, DoubleJumpC2SPayload> CODEC = StreamCodec.composite(ByteBufCodecs.INT, DoubleJumpC2SPayload::entityID, DoubleJumpC2SPayload::new);

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return ID;
    }
}
