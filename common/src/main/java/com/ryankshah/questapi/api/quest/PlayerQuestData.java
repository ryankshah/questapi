package com.ryankshah.questapi.api.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.UUIDUtil;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * All quest progress for a single player, keyed by quest ID.
 * <p>
 * Quests the player has never interacted with are not present in {@link #progress} at all; callers
 * should treat a missing entry as {@link QuestState#LOCKED} (or {@code AVAILABLE} if the quest has no
 * prerequisites), which {@code QuestManager} materialises lazily rather than eagerly creating an
 * entry for every registered quest for every player.
 */
public final class PlayerQuestData {

    public static final Codec<PlayerQuestData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            UUIDUtil.CODEC.fieldOf("player").forGetter(PlayerQuestData::playerId),

            Codec.unboundedMap(Identifier.CODEC, QuestProgress.CODEC).fieldOf("quests").forGetter(PlayerQuestData::progressRaw)
    ).apply(instance, PlayerQuestData::new));

    private final UUID playerId;
    private final Map<Identifier, QuestProgress> progress;

    public PlayerQuestData(UUID playerId, Map<Identifier, QuestProgress> progress) {
        this.playerId = playerId;
        this.progress = new HashMap<>(progress);
    }

    public static PlayerQuestData empty(UUID playerId) {
        return new PlayerQuestData(playerId, Map.of());
    }

    public UUID playerId() {
        return playerId;
    }

    public Map<Identifier, QuestProgress> progress() {
        return progress;
    }

    private Map<Identifier, QuestProgress> progressRaw() {
        return progress;
    }

    public QuestProgress get(Identifier questId) {
        return progress.get(questId);
    }

    public QuestProgress getOrCreate(Identifier questId) {
        return progress.computeIfAbsent(questId, id -> QuestProgress.locked());
    }

    /**
     * Deep copy, safe to hand to something that will read it later (or on another thread) without
     * racing further mutations - e.g. a network payload that gets encoded asynchronously well after
     * the call that queued it returns.
     */
    public PlayerQuestData copy() {
        Map<Identifier, QuestProgress> copiedProgress = new HashMap<>();
        for (Map.Entry<Identifier, QuestProgress> entry : progress.entrySet()) {
            copiedProgress.put(entry.getKey(), entry.getValue().copy());
        }
        return new PlayerQuestData(playerId, copiedProgress);
    }
}
