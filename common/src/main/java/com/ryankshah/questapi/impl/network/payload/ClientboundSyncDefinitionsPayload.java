package com.ryankshah.questapi.impl.network.payload;

import com.ryankshah.questapi.QuestApi;
import com.ryankshah.questapi.api.quest.Quest;
import com.ryankshah.questapi.api.quest.QuestCategory;
import com.ryankshah.questapi.impl.network.QuestCodecs;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

import java.util.List;

/**
 * Sent to a client once, right after login (and again if dev-mode reloads quest definitions),
 * carrying every registered category and quest definition so the default GUI never needs to ask the
 * server for static content while it is open.
 */
public record ClientboundSyncDefinitionsPayload(List<QuestCategory> categories, List<Quest> quests) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ClientboundSyncDefinitionsPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(QuestApi.MOD_ID, "sync_definitions"));

    public static final StreamCodec<ByteBuf, ClientboundSyncDefinitionsPayload> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.fromCodec(QuestCategory.CODEC.listOf()), ClientboundSyncDefinitionsPayload::categories,
            ByteBufCodecs.fromCodec(QuestCodecs.questCodec(QuestApi.registry()).listOf()), ClientboundSyncDefinitionsPayload::quests,
            ClientboundSyncDefinitionsPayload::new
    );

    @Override
    public CustomPacketPayload.Type<ClientboundSyncDefinitionsPayload> type() {
        return TYPE;
    }
}
