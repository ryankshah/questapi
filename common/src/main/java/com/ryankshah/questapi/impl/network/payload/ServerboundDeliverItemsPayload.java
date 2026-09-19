package com.ryankshah.questapi.impl.network.payload;

import com.ryankshah.questapi.QuestApi;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Requests that the server remove {@code amount} items from the sending player's inventory and
 * credit them to a delivery objective.
 */
public record ServerboundDeliverItemsPayload(Identifier questId, int objectiveIndex, int amount) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ServerboundDeliverItemsPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(QuestApi.MOD_ID, "deliver_items"));

    public static final StreamCodec<ByteBuf, ServerboundDeliverItemsPayload> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, ServerboundDeliverItemsPayload::questId,
            ByteBufCodecs.VAR_INT, ServerboundDeliverItemsPayload::objectiveIndex,
            ByteBufCodecs.VAR_INT, ServerboundDeliverItemsPayload::amount,
            ServerboundDeliverItemsPayload::new
    );

    @Override
    public CustomPacketPayload.Type<ServerboundDeliverItemsPayload> type() {
        return TYPE;
    }
}
