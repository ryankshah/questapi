package com.ryankshah.questapi.api.quest;

/**
 * How a repeatable quest's cooldown is measured, see {@link Quest.Builder#repeatable(ResetMode, int)}.
 */
public enum ResetMode {

    /**
     * Resets a fixed number of real hours after the quest was last claimed, checked against system
     * time. The check happens whenever the player next interacts with quests, not at the exact
     * moment the cooldown expires - resets still happen even if the player was offline for it.
     */
    WALL_CLOCK,

    /**
     * Resets after a fixed number of in-game days have elapsed since the quest was last claimed,
     * measured against the world's age (total elapsed ticks / 24000). Only advances while the world
     * is actually running, so a server that sits offline does not "catch up" the same way wall-clock
     * quests do.
     */
    IN_GAME_DAY
}
