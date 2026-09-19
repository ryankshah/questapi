package com.ryankshah.questapi.api.quest.condition;

import com.ryankshah.questapi.api.quest.QuestContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

/**
 * A predicate a player must satisfy for a quest to unlock (become {@code AVAILABLE} instead of
 * {@code LOCKED}). Conditions are evaluated server-side, every time a player's quest availability
 * is recomputed (login, quest completion, level change, etc).
 * <p>
 * A quest's full set of conditions must <em>all</em> pass for it to unlock; this includes the
 * implicit "other quest completed" prerequisites as well as any additional conditions such as
 * advancement or item requirements.
 */
public interface QuestCondition {

    Identifier typeId();

    /**
     * Short human-readable description shown in the GUI while the quest is locked,
     * e.g. "Requires: Stone Age".
     */
    Component describe();

    boolean test(QuestContext context);
}
