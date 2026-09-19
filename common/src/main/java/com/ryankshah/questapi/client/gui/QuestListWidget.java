package com.ryankshah.questapi.client.gui;

import com.ryankshah.questapi.api.quest.Quest;
import com.ryankshah.questapi.api.quest.QuestState;
import com.ryankshah.questapi.client.ClientQuestDataCache;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;

import java.util.List;
import java.util.function.Consumer;

/**
 * The scrollable list of quests within the currently selected category.
 */
public final class QuestListWidget extends ObjectSelectionList<QuestListEntry> {

    private final Consumer<Quest> onSelect;

    public QuestListWidget(Minecraft minecraft, int x, int y, int width, int height, Consumer<Quest> onSelect) {
        super(minecraft, width, height, y, 34);
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

    public void setQuests(List<Quest> quests, ClientQuestDataCache cache) {
        clearEntries();
        for (Quest quest : quests) {
            QuestState state = cache.getState(quest.id());
            addEntry(new QuestListEntry(quest, state));
        }
    }

    @Override
    public void setSelected(QuestListEntry entry) {
        super.setSelected(entry);
        if (entry != null) {
            onSelect.accept(entry.quest());
        }
    }
}
