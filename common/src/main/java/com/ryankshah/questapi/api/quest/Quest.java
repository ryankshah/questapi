package com.ryankshah.questapi.api.quest;

import com.ryankshah.questapi.api.quest.condition.QuestCondition;
import com.ryankshah.questapi.api.quest.objective.ObjectiveDefinition;
import com.ryankshah.questapi.api.quest.reward.QuestReward;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * Immutable, static definition of a quest: its identity, presentation, objectives, rewards and
 * unlock conditions. Holds no player-specific state whatsoever - see {@link QuestProgress} for that.
 * <p>
 * Build one with {@link #builder(Identifier)}.
 */
public final class Quest {

    private final Identifier id;
    private final Component title;
    private final Component description;
    private final ItemStack icon;
    private final Identifier categoryId;
    private final List<ObjectiveDefinition> objectives;
    private final List<QuestReward> rewards;
    private final List<QuestCondition> prerequisites;
    private final boolean autoActivate;
    private final int sortOrder;

    private Quest(Builder builder) {
        this.id = builder.id;
        this.title = builder.title;
        this.description = builder.description;
        this.icon = builder.icon;
        this.categoryId = builder.categoryId;
        this.objectives = List.copyOf(builder.objectives);
        this.rewards = List.copyOf(builder.rewards);
        this.prerequisites = List.copyOf(builder.prerequisites);
        this.autoActivate = builder.autoActivate;
        this.sortOrder = builder.sortOrder;
    }

    public static Builder builder(Identifier id) {
        return new Builder(id);
    }

    public Identifier id() {
        return id;
    }

    public Component title() {
        return title;
    }

    public Component description() {
        return description;
    }

    public ItemStack icon() {
        return icon;
    }

    public Identifier categoryId() {
        return categoryId;
    }

    public List<ObjectiveDefinition> objectives() {
        return objectives;
    }

    public List<QuestReward> rewards() {
        return rewards;
    }

    public List<QuestCondition> prerequisites() {
        return prerequisites;
    }

    /**
     * If {@code true}, this quest skips the {@code AVAILABLE} state and becomes {@code ACTIVE} the
     * moment its prerequisites are satisfied, without requiring the player to manually start it.
     */
    public boolean autoActivate() {
        return autoActivate;
    }

    /**
     * Lower values are displayed first within a category.
     */
    public int sortOrder() {
        return sortOrder;
    }

    public static final class Builder {
        private final Identifier id;
        private Component title = Component.literal("Untitled Quest");
        private Component description = Component.empty();
        private ItemStack icon = ItemStack.EMPTY;
        private Identifier categoryId;
        private final List<ObjectiveDefinition> objectives = new java.util.ArrayList<>();
        private final List<QuestReward> rewards = new java.util.ArrayList<>();
        private final List<QuestCondition> prerequisites = new java.util.ArrayList<>();
        private boolean autoActivate = false;
        private int sortOrder = 0;

        private Builder(Identifier id) {
            this.id = id;
        }

        public Builder title(Component title) {
            this.title = title;
            return this;
        }

        public Builder description(Component description) {
            this.description = description;
            return this;
        }

        public Builder icon(ItemStack icon) {
            this.icon = icon;
            return this;
        }

        public Builder category(Identifier categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder objective(ObjectiveDefinition objective) {
            this.objectives.add(objective);
            return this;
        }

        public Builder objectives(List<ObjectiveDefinition> objectives) {
            this.objectives.addAll(objectives);
            return this;
        }

        public Builder reward(QuestReward reward) {
            this.rewards.add(reward);
            return this;
        }

        public Builder rewards(List<QuestReward> rewards) {
            this.rewards.addAll(rewards);
            return this;
        }

        public Builder requires(QuestCondition condition) {
            this.prerequisites.add(condition);
            return this;
        }

        public Builder requires(List<QuestCondition> conditions) {
            this.prerequisites.addAll(conditions);
            return this;
        }

        public Builder autoActivate(boolean autoActivate) {
            this.autoActivate = autoActivate;
            return this;
        }

        public Builder sortOrder(int sortOrder) {
            this.sortOrder = sortOrder;
            return this;
        }

        public Quest build() {
            if (categoryId == null) {
                throw new IllegalStateException("Quest " + id + " has no category assigned");
            }
            if (objectives.isEmpty()) {
                throw new IllegalStateException("Quest " + id + " has no objectives");
            }
            return new Quest(this);
        }
    }
}
