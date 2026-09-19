package com.ryankshah.questapi.api.quest;

import com.ryankshah.questapi.api.QuestManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;

/**
 * Execution context handed to objective evaluators, reward grants and quest conditions.
 * <p>
 * All quest logic is server-authoritative, so this context is always backed by a real
 * {@link ServerPlayer}; there is deliberately no client-side equivalent.
 */
public record QuestContext(ServerPlayer player, QuestManager manager) {

    public MinecraftServer server() {
        return player.level().getServer();
    }

    public ServerLevel level() {
        return player.level();
    }
}
