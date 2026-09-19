package com.ryankshah.questapi.api;

import com.mojang.serialization.Codec;
import com.ryankshah.questapi.api.quest.Quest;
import com.ryankshah.questapi.api.quest.QuestCategory;
import com.ryankshah.questapi.api.quest.condition.ConditionType;
import com.ryankshah.questapi.api.quest.condition.QuestCondition;
import com.ryankshah.questapi.api.quest.objective.ObjectiveDefinition;
import com.ryankshah.questapi.api.quest.objective.ObjectiveType;
import com.ryankshah.questapi.api.quest.reward.QuestReward;
import com.ryankshah.questapi.api.quest.reward.RewardType;
import net.minecraft.resources.Identifier;

import java.util.Collection;
import java.util.Optional;

/**
 * The static side of the Questing API: quest definitions, categories, and the extensible type
 * registries for objectives, rewards and conditions.
 * <p>
 * Obtain the shared instance via {@link com.ryankshah.questapi.QuestApi#registry()}. Register
 * content during your mod's common initialization phase - registration after the server has started
 * is not supported since quest definitions are synced to clients once, at login.
 */
public interface QuestRegistry {

    void registerCategory(QuestCategory category);

    Collection<QuestCategory> categories();

    Optional<QuestCategory> getCategory(Identifier id);

    /**
     * Removes a single category, e.g. when a datapack file that previously defined it is removed on
     * reload. Does not touch the quests that reference it.
     */
    void removeCategory(Identifier id);

    void registerQuest(Quest quest);

    Collection<Quest> quests();

    Optional<Quest> getQuest(Identifier id);

    /**
     * Removes a single quest, e.g. when a datapack file that previously defined it is removed on
     * reload.
     */
    void removeQuest(Identifier id);

    Collection<Quest> questsInCategory(Identifier categoryId);

    <D extends ObjectiveDefinition> void registerObjectiveType(ObjectiveType<D> type);

    Optional<ObjectiveType<?>> getObjectiveType(Identifier id);

    Collection<ObjectiveType<?>> objectiveTypes();

    <R extends QuestReward> void registerRewardType(RewardType<R> type);

    Optional<RewardType<?>> getRewardType(Identifier id);

    <C extends QuestCondition> void registerConditionType(ConditionType<C> type);

    Optional<ConditionType<?>> getConditionType(Identifier id);

    /**
     * A {@link Codec} that dispatches on a condition's registered type, resolving nested
     * conditions by type ID exactly like {@code Quest}'s own prerequisite list does. Exists for
     * composite condition types (e.g. an "any of" wrapper holding other conditions) that need to
     * (de)serialize a nested {@link QuestCondition} without depending on this module's network code.
     * Resolution is lazy - safe to call while building a composite type's own codec, even before
     * every condition type (including that composite type itself) has finished registering.
     */
    default Codec<QuestCondition> conditionCodec() {
        return Identifier.CODEC.dispatch("type", QuestCondition::typeId, id -> getConditionType(id)
                .map(ConditionType::codec)
                .orElseThrow(() -> new IllegalArgumentException("Unknown condition type: " + id)));
    }

    /**
     * Removes every registered quest and category. Intended for dev-mode quest reloading; not
     * exposed to production commands.
     */
    void clearQuests();
}
