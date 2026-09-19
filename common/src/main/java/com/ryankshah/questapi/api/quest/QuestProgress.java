package com.ryankshah.questapi.api.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ryankshah.questapi.api.quest.objective.ObjectiveProgress;

import java.util.HashMap;
import java.util.Map;

/**
 * Mutable, per-player runtime state for a single quest.
 * <p>
 * Instances live inside {@link PlayerQuestData} and are persisted to disk; the paired
 * {@link Quest} definition is looked up by ID from the {@link com.ryankshah.questapi.api.QuestRegistry}
 * rather than being duplicated here.
 */
public final class QuestProgress {

    public static final Codec<QuestProgress> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.xmap(QuestState::valueOf, Enum::name).fieldOf("state").forGetter(QuestProgress::state),
            Codec.unboundedMap(Codec.STRING.xmap(Integer::parseInt, String::valueOf), ObjectiveProgress.CODEC)
                    .fieldOf("objectives").forGetter(QuestProgress::objectivesRaw),
            Codec.LONG.fieldOf("startedAt").forGetter(QuestProgress::startedAt),
            Codec.LONG.fieldOf("completedAt").forGetter(QuestProgress::completedAt),
            Codec.LONG.fieldOf("rewardedAt").forGetter(QuestProgress::rewardedAt)
    ).apply(instance, QuestProgress::new));

    private QuestState state;
    private final Map<Integer, ObjectiveProgress> objectives;
    private long startedAt;
    private long completedAt;
    private long rewardedAt;

    public QuestProgress(QuestState state, Map<Integer, ObjectiveProgress> objectives, long startedAt, long completedAt, long rewardedAt) {
        this.state = state;
        this.objectives = new HashMap<>(objectives);
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.rewardedAt = rewardedAt;
    }

    public static QuestProgress locked() {
        return new QuestProgress(QuestState.LOCKED, Map.of(), 0, 0, 0);
    }

    public QuestState state() {
        return state;
    }

    public void setState(QuestState state) {
        this.state = state;
    }

    public Map<Integer, ObjectiveProgress> objectives() {
        return objectives;
    }

    private Map<Integer, ObjectiveProgress> objectivesRaw() {
        return objectives;
    }

    public ObjectiveProgress objective(int index) {
        return objectives.computeIfAbsent(index, i -> ObjectiveProgress.empty());
    }

    public long startedAt() {
        return startedAt;
    }

    public void setStartedAt(long startedAt) {
        this.startedAt = startedAt;
    }

    public long completedAt() {
        return completedAt;
    }

    public void setCompletedAt(long completedAt) {
        this.completedAt = completedAt;
    }

    public long rewardedAt() {
        return rewardedAt;
    }

    public void setRewardedAt(long rewardedAt) {
        this.rewardedAt = rewardedAt;
    }

    public boolean rewardClaimed() {
        return state == QuestState.REWARDED;
    }

    /**
     * Deep copy, safe to hand to something that will read it later (or on another thread) without
     * racing further mutations - e.g. a network payload that gets encoded asynchronously well after
     * the call that queued it returns.
     */
    public QuestProgress copy() {
        Map<Integer, ObjectiveProgress> copiedObjectives = new HashMap<>();
        for (Map.Entry<Integer, ObjectiveProgress> entry : objectives.entrySet()) {
            copiedObjectives.put(entry.getKey(), entry.getValue().copy());
        }
        return new QuestProgress(state, copiedObjectives, startedAt, completedAt, rewardedAt);
    }
}
