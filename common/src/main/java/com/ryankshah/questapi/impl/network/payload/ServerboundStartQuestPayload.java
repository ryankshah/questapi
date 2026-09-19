package com.ryankshah.questapi.impl.network.payload;

import com.ryankshah.questapi.QuestApi;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Requests that the server manually start an {@code AVAILABLE} quest for the sending player.
 */
public record ServerboundStartQuestPayload(Identifier questId) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ServerboundStartQuestPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(QuestApi.MOD_ID, "start_quest"));

    public static final StreamCodec<ByteBuf, ServerboundStartQuestPayload> STREAM_CODEC =
            Identifier.STREAM_CODEC.map(ServerboundStartQuestPayload::new, ServerboundStartQuestPayload::questId);

    @Override
    public CustomPacketPayload.Type<ServerboundStartQuestPayload> type() {
        return TYPE;
    }
}
