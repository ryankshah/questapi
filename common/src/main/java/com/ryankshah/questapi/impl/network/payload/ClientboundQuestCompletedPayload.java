package com.ryankshah.questapi.impl.network.payload;

import com.ryankshah.questapi.QuestApi;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Sent to a client the moment one of their quests becomes {@code COMPLETED}, purely to drive the
 * completion sound and toast - separate from {@link ClientboundSyncProgressPayload} so the GUI's
 * progress cache and the one-shot feedback effect stay independent concerns.
 */
public record ClientboundQuestCompletedPayload(Component questTitle) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClientboundQuestCompletedPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(QuestApi.MOD_ID, "quest_completed"));

    public static final StreamCodec<ByteBuf, ClientboundQuestCompletedPayload> STREAM_CODEC =
            ByteBufCodecs.fromCodec(ComponentSerialization.CODEC).map(ClientboundQuestCompletedPayload::new, ClientboundQuestCompletedPayload::questTitle);

    @Override
    public CustomPacketPayload.Type<ClientboundQuestCompletedPayload> type() {
        return TYPE;
    }
}
