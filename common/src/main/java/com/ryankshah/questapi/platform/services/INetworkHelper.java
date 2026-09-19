package com.ryankshah.questapi.platform.services;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;

/**
 * Loader-specific abstraction over sending custom payloads. Packet <em>type</em> registration is
 * still done separately per loader (Fabric's {@code PayloadTypeRegistry}, NeoForge's
 * {@code RegisterPayloadHandlersEvent}) since the two APIs differ too much to unify meaningfully,
 * but sending an already-registered payload is identical in spirit on both, so common code can go
 * through this interface instead of caring which loader it's running on.
 */
public interface INetworkHelper {

    void sendToServer(CustomPacketPayload payload);

    void sendToPlayer(ServerPlayer player, CustomPacketPayload payload);
}
