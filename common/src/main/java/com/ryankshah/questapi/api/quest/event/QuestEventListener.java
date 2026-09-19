package com.ryankshah.questapi.api.quest.event;

import com.ryankshah.questapi.api.quest.Quest;
import com.ryankshah.questapi.api.quest.objective.ObjectiveProgress;
import net.minecraft.server.level.ServerPlayer;

/**
 * Hook interface for observing quest lifecycle events from other mods. Register an implementation
 * via {@link QuestEvents#register}; every method has a no-op default so listeners only need to
 * override the events they actually care about.
 * <p>
 * All callbacks fire server-side, synchronously with the state change that triggered them.
 */
public interface QuestEventListener {

    /**
     * Fired once when a quest is added to the registry, typically during mod initialization.
     */
    default void onQuestRegistered(Quest quest) {
    }

    default void onQuestStarted(ServerPlayer player, Quest quest) {
    }

    default void onObjectiveProgressChanged(ServerPlayer player, Quest quest, int objectiveIndex, ObjectiveProgress progress) {
    }

    default void onObjectiveCompleted(ServerPlayer player, Quest quest, int objectiveIndex) {
    }

    default void onQuestCompleted(ServerPlayer player, Quest quest) {
    }

    default void onRewardClaimed(ServerPlayer player, Quest quest) {
    }

    default void onQuestReset(ServerPlayer player, Quest quest) {
    }
}
