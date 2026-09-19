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
 * Sent to a client the moment one of their quests becomes visible to them for the first time -
 * newly {@code AVAILABLE}, or straight to {@code ACTIVE} for an {@code autoActivate} quest - purely
 * to drive a "new quest" sound and toast, separate from {@link ClientboundSyncProgressPayload}.
 */
public record ClientboundQuestUnlockedPayload(Component questTitle) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClientboundQuestUnlockedPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(QuestApi.MOD_ID, "quest_unlocked"));

    public static final StreamCodec<ByteBuf, ClientboundQuestUnlockedPayload> STREAM_CODEC =
            ByteBufCodecs.fromCodec(ComponentSerialization.CODEC).map(ClientboundQuestUnlockedPayload::new, ClientboundQuestUnlockedPayload::questTitle);

    @Override
    public CustomPacketPayload.Type<ClientboundQuestUnlockedPayload> type() {
        return TYPE;
    }
}
