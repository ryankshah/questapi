package com.ryankshah.questapi.impl.network.payload;

import com.ryankshah.questapi.QuestApi;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Requests that the server claim the rewards of a {@code COMPLETED} quest for the sending player.
 */
public record ServerboundClaimRewardPayload(Identifier questId) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ServerboundClaimRewardPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(QuestApi.MOD_ID, "claim_reward"));

    public static final StreamCodec<ByteBuf, ServerboundClaimRewardPayload> STREAM_CODEC =
            Identifier.STREAM_CODEC.map(ServerboundClaimRewardPayload::new, ServerboundClaimRewardPayload::questId);

    @Override
    public CustomPacketPayload.Type<ServerboundClaimRewardPayload> type() {
        return TYPE;
    }
}
