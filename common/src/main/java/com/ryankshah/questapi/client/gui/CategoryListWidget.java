package com.ryankshah.questapi.client.gui;

import com.ryankshah.questapi.api.quest.QuestCategory;
import com.ryankshah.questapi.api.quest.QuestState;
import com.ryankshah.questapi.client.ClientQuestDataCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;

import java.util.List;
import java.util.function.Consumer;

/**
 * Scrollable sidebar of quest categories, replacing a fixed row of tabs so it never overflows the
 * panel regardless of how many categories are registered.
 */
public final class CategoryListWidget extends ObjectSelectionList<CategoryListEntry> {

    private final Consumer<QuestCategory> onSelect;

    public CategoryListWidget(Minecraft minecraft, int x, int y, int width, int height, Consumer<QuestCategory> onSelect) {
        super(minecraft, width, height, y, 28);
        this.onSelect = onSelect;
        updateSizeAndPosition(width, height, x, y);
    }

    @Override
    public int getRowWidth() {
        return this.width - 8;
    }

    @Override
    protected void extractListBackground(GuiGraphicsExtractor graphics) {
        // The vanilla menu-list background/separator textures clash with our own flat panel
        // background (QuestScreen already fills this area), so skip them entirely.
    }

    @Override
    protected void extractListSeparators(GuiGraphicsExtractor graphics) {
    }

    public void setCategories(List<QuestCategory> categories, ClientQuestDataCache cache) {
        clearEntries();
        for (QuestCategory category : categories) {
            boolean needsAttention = cache.questsInCategory(category.id()).stream()
                    .anyMatch(quest -> cache.getState(quest.id()) == QuestState.COMPLETED);
            addEntry(new CategoryListEntry(category, needsAttention));
        }
    }

    @Override
    public void setSelected(CategoryListEntry entry) {
        super.setSelected(entry);
        if (entry != null) {
            onSelect.accept(entry.category());
        }
    }
}
