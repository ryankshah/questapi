package com.ryankshah.questapi.impl.network.payload;

import com.ryankshah.questapi.QuestApi;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Requests that the server abandon an {@code ACTIVE} quest for the sending player.
 */
public record ServerboundAbandonQuestPayload(Identifier questId) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ServerboundAbandonQuestPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(QuestApi.MOD_ID, "abandon_quest"));

    public static final StreamCodec<ByteBuf, ServerboundAbandonQuestPayload> STREAM_CODEC =
            Identifier.STREAM_CODEC.map(ServerboundAbandonQuestPayload::new, ServerboundAbandonQuestPayload::questId);

    @Override
    public CustomPacketPayload.Type<ServerboundAbandonQuestPayload> type() {
        return TYPE;
    }
}
