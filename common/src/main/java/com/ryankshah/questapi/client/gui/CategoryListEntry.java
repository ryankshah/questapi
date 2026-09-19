package com.ryankshah.questapi.client.gui;

import com.ryankshah.questapi.api.quest.QuestCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;

/**
 * A single entry in the {@link CategoryListWidget} sidebar. Text-only (word-wrapped to the column
 * width) since categories are meant to be short labels, not icon-heavy buttons - this also means the
 * sidebar scales to however many categories a mod registers instead of overflowing a fixed-width
 * row of tabs.
 */
public final class CategoryListEntry extends ObjectSelectionList.Entry<CategoryListEntry> {

    private final QuestCategory category;

    public CategoryListEntry(QuestCategory category) {
        this.category = category;
    }

    public QuestCategory category() {
        return category;
    }

    @Override
    public Component getNarration() {
        return category.displayName();
    }

    @Override
    public void extractContent(GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float partialTick) {
        int x = getX();
        int y = getY();
        int width = getWidth();
        int height = getHeight();

        if (hovered) {
            graphics.fill(x, y, x + width, y + height, 0x40FFFFFF);
        }

        var font = Minecraft.getInstance().font;
        var lines = font.split(category.displayName(), width - 6);
        int totalHeight = lines.size() * 10;
        int lineY = y + Math.max(2, (height - totalHeight) / 2);
        for (var line : lines) {
            graphics.text(font, line, x + 3, lineY, 0xFFFFFFFF);
            lineY += 10;
        }
    }
}
