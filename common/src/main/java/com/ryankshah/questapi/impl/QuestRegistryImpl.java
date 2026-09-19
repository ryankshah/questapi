package com.ryankshah.questapi.impl;

import com.ryankshah.questapi.api.QuestRegistry;
import com.ryankshah.questapi.api.quest.Quest;
import com.ryankshah.questapi.api.quest.QuestCategory;
import com.ryankshah.questapi.api.quest.condition.ConditionType;
import com.ryankshah.questapi.api.quest.condition.QuestCondition;
import com.ryankshah.questapi.api.quest.objective.ObjectiveDefinition;
import com.ryankshah.questapi.api.quest.objective.ObjectiveType;
import com.ryankshah.questapi.api.quest.event.QuestEventListener;
import com.ryankshah.questapi.api.quest.event.QuestEvents;
import com.ryankshah.questapi.api.quest.reward.QuestReward;
import com.ryankshah.questapi.api.quest.reward.RewardType;
import net.minecraft.resources.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public final class QuestRegistryImpl implements QuestRegistry {

    private final Map<Identifier, QuestCategory> categories = new LinkedHashMap<>();
    private final Map<Identifier, Quest> quests = new LinkedHashMap<>();
    private final Map<Identifier, ObjectiveType<?>> objectiveTypes = new LinkedHashMap<>();
    private final Map<Identifier, RewardType<?>> rewardTypes = new LinkedHashMap<>();
    private final Map<Identifier, ConditionType<?>> conditionTypes = new LinkedHashMap<>();

    @Override
    public void registerCategory(QuestCategory category) {
        categories.put(category.id(), category);
    }

    @Override
    public Collection<QuestCategory> categories() {
        return categories.values();
    }

    @Override
    public Optional<QuestCategory> getCategory(Identifier id) {
        return Optional.ofNullable(categories.get(id));
    }

    @Override
    public void removeCategory(Identifier id) {
        categories.remove(id);
    }

    @Override
    public void registerQuest(Quest quest) {
        if (!categories.containsKey(quest.categoryId())) {
            throw new IllegalStateException("Quest " + quest.id() + " references unregistered category " + quest.categoryId());
        }
        quests.put(quest.id(), quest);
        for (QuestEventListener listener : QuestEvents.listeners()) {
            listener.onQuestRegistered(quest);
        }
    }

    @Override
    public Collection<Quest> quests() {
        return quests.values();
    }

    @Override
    public Optional<Quest> getQuest(Identifier id) {
        return Optional.ofNullable(quests.get(id));
    }

    @Override
    public void removeQuest(Identifier id) {
        quests.remove(id);
    }

    @Override
    public Collection<Quest> questsInCategory(Identifier categoryId) {
        List<Quest> result = new ArrayList<>();
        for (Quest quest : quests.values()) {
            if (quest.categoryId().equals(categoryId)) {
                result.add(quest);
            }
        }
        result.sort((a, b) -> Integer.compare(a.sortOrder(), b.sortOrder()));
        return result;
    }

    @Override
    public <D extends ObjectiveDefinition> void registerObjectiveType(ObjectiveType<D> type) {
        objectiveTypes.put(type.id(), type);
    }

    @Override
    public Optional<ObjectiveType<?>> getObjectiveType(Identifier id) {
        return Optional.ofNullable(objectiveTypes.get(id));
    }

    @Override
    public Collection<ObjectiveType<?>> objectiveTypes() {
        return objectiveTypes.values();
    }

    @Override
    public <R extends QuestReward> void registerRewardType(RewardType<R> type) {
        rewardTypes.put(type.id(), type);
    }

    @Override
    public Optional<RewardType<?>> getRewardType(Identifier id) {
        return Optional.ofNullable(rewardTypes.get(id));
    }

    @Override
    public <C extends QuestCondition> void registerConditionType(ConditionType<C> type) {
        conditionTypes.put(type.id(), type);
    }

    @Override
    public Optional<ConditionType<?>> getConditionType(Identifier id) {
        return Optional.ofNullable(conditionTypes.get(id));
    }

    @Override
    public void clearQuests() {
        quests.clear();
        categories.clear();
    }
}
