package com.ryankshah.questapi.impl.network.payload;

import com.ryankshah.questapi.QuestApi;
import com.ryankshah.questapi.api.quest.PlayerQuestData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Sent to a client whenever their own quest progress changes server-side (login, objective
 * progress, completion, reward claim, reset, ...), carrying a full snapshot of their
 * {@link PlayerQuestData}.
 * <p>
 * A full snapshot rather than a delta keeps the client's cache trivially consistent and is cheap
 * enough in practice - progress data is a handful of small integers per objective, not bulk content.
 */
public record ClientboundSyncProgressPayload(PlayerQuestData data) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClientboundSyncProgressPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(QuestApi.MOD_ID, "sync_progress"));

    public static final StreamCodec<ByteBuf, ClientboundSyncProgressPayload> STREAM_CODEC =
            ByteBufCodecs.fromCodec(PlayerQuestData.CODEC).map(ClientboundSyncProgressPayload::new, ClientboundSyncProgressPayload::data);

    @Override
    public CustomPacketPayload.Type<ClientboundSyncProgressPayload> type() {
        return TYPE;
    }
}
