package com.ryankshah.questapi.impl.network.payload;

import com.ryankshah.questapi.QuestApi;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

/**
 * Asks the server to resend both quest definitions and the sending player's progress, e.g. when the
 * default GUI is opened after having missed an update.
 */
public record ServerboundRequestSyncPayload() implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<ServerboundRequestSyncPayload> TYPE =
            new CustomPacketPayload.Type<>(Identifier.fromNamespaceAndPath(QuestApi.MOD_ID, "request_sync"));

    public static final StreamCodec<ByteBuf, ServerboundRequestSyncPayload> STREAM_CODEC =
            StreamCodec.unit(new ServerboundRequestSyncPayload());

    @Override
    public CustomPacketPayload.Type<ServerboundRequestSyncPayload> type() {
        return TYPE;
    }
}
