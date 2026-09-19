package com.ryankshah.questapi.api.quest.objective;

import com.ryankshah.questapi.api.quest.QuestContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

/**
 * Static, immutable description of a single quest objective.
 * <p>
 * Implementations must not hold any player-specific mutable state; runtime progress lives in the
 * paired {@link ObjectiveProgress} instance which is passed alongside this definition to
 * {@link #evaluate}.
 * <p>
 * Two evaluation styles are supported through the same method:
 * <ul>
 *     <li><b>Poll-based</b> objectives (item possession, mining/crafting/kill counts, exploration)
 *     ignore {@code eventKey} and recompute their absolute progress from current game state every
 *     time they are called, which happens roughly once per second for active quests.</li>
 *     <li><b>Push-based</b> objectives (block placement, taming) only react to their specific
 *     {@code eventKey} and otherwise return {@code progress.current()} unchanged.</li>
 * </ul>
 */
public interface ObjectiveDefinition {

    /**
     * The registered {@link ObjectiveType} identifier for this definition.
     */
    Identifier typeId();

    /**
     * Human-readable description of the objective, e.g. "Obtain 10 Diamond".
     */
    Component describe();

    /**
     * The amount of progress required to complete this objective.
     */
    int targetAmount();

    /**
     * Icon rendered next to this objective in the default GUI.
     */
    ItemStack icon();

    /**
     * Computes the new absolute progress amount for this objective.
     *
     * @param context    the evaluating player's context
     * @param progress   the current runtime progress (must not be mutated directly; return the new value)
     * @param eventKey   {@link ObjectiveEventKeys#TICK} for a periodic poll, or a specific event key
     *                   for a push-based update
     * @param eventValue an event-specific magnitude (e.g. the number of blocks placed in this event),
     *                   meaningless for poll-based evaluation
     * @return the new absolute progress amount, which will be clamped to {@code [0, targetAmount()]}
     */
    int evaluate(QuestContext context, ObjectiveProgress progress, Identifier eventKey, int eventValue);

    /**
     * Called once when the objective's quest becomes {@code ACTIVE}. Used by stat-delta objectives
     * (mining, crafting, kills) to record a baseline so that pre-existing statistics do not
     * instantly complete the objective.
     */
    default int captureBaseline(QuestContext context) {
        return 0;
    }
}
