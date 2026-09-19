package com.ryankshah.questapi.api.quest;

/**
 * The lifecycle state of a single quest for a single player.
 * <pre>
 * LOCKED -----------> AVAILABLE -----------> ACTIVE -----------> COMPLETED -----------> REWARDED
 *   ^  prerequisites       |  start()            |  objectives         |  claimReward()
 *   |  not met yet         |  (auto or manual)    |  all met            |
 *   |                      |                      |                     |
 *   +----------------------+----------------------+---------------------+
 *                       resetQuest() returns a quest to LOCKED or AVAILABLE
 * </pre>
 * <ul>
 *     <li>{@link #LOCKED} - one or more {@link com.ryankshah.questapi.api.quest.condition.QuestCondition}
 *     prerequisites are not yet satisfied. Hidden or greyed out in the GUI depending on configuration.</li>
 *     <li>{@link #AVAILABLE} - prerequisites are satisfied but the player has not started the quest.
 *     Quests marked {@code autoActivate} skip this state entirely and jump straight to {@link #ACTIVE}.</li>
 *     <li>{@link #ACTIVE} - the quest has been started and its objectives are being tracked.</li>
 *     <li>{@link #COMPLETED} - every objective has reached its target amount. Rewards have not yet
 *     been granted.</li>
 *     <li>{@link #REWARDED} - the player has claimed the quest's rewards. Terminal state; a quest
 *     only leaves it via an explicit reset.</li>
 *     <li>{@link #ABANDONED} - the player cancelled an in-progress quest. Functionally equivalent to
 *     {@link #AVAILABLE} (or {@link #LOCKED} if prerequisites regressed) but preserved as a distinct
 *     state for UI/analytics purposes.</li>
 * </ul>
 */
public enum QuestState {
    LOCKED,
    AVAILABLE,
    ACTIVE,
    COMPLETED,
    REWARDED,
    ABANDONED
}
