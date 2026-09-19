package com.ryankshah.questapi.api.quest;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.item.ItemStack;

/**
 * A named grouping of quests shown as a tab/column in the default quest GUI.
 * <p>
 * Categories are purely organisational; a quest's prerequisites and unlock state are independent
 * of which category it belongs to.
 *
 * @param id          namespaced identifier of this category
 * @param displayName the text shown in the category selector
 * @param icon        the item stack rendered as this category's icon
 * @param sortOrder   lower values are displayed first
 */
public record QuestCategory(Identifier id, Component displayName, ItemStack icon, int sortOrder) {

    public static final Codec<QuestCategory> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("id").forGetter(QuestCategory::id),
            ComponentSerialization.CODEC.fieldOf("display_name").forGetter(QuestCategory::displayName),
            ItemStack.CODEC.fieldOf("icon").forGetter(QuestCategory::icon),
            Codec.INT.fieldOf("sort_order").forGetter(QuestCategory::sortOrder)
    ).apply(instance, QuestCategory::new));

    public static QuestCategory of(Identifier id, Component displayName, ItemStack icon) {
        return new QuestCategory(id, displayName, icon, 0);
    }

    public QuestCategory withSortOrder(int order) {
        return new QuestCategory(id, displayName, icon, order);
    }
}
