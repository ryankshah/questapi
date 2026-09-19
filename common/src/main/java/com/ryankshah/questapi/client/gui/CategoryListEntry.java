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
    private final Component displayText;

    /**
     * @param needsAttention {@code true} if a quest in this category is {@code COMPLETED} and
     *                       awaiting its reward claim - shown as a star next to the category name,
     *                       the same marker used for an individual completed quest.
     */
    public CategoryListEntry(QuestCategory category, boolean needsAttention) {
        this.category = category;
        this.displayText = needsAttention
                ? category.displayName().copy().append(Component.literal(" ★").withStyle(style -> style.withColor(0xFFAA00)))
                : category.displayName();
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
        var lines = font.split(displayText, width - 6);
        int totalHeight = lines.size() * 10;
        int lineY = y + Math.max(2, (height - totalHeight) / 2);
        for (var line : lines) {
            graphics.text(font, line, x + 3, lineY, 0xFFFFFFFF);
            lineY += 10;
        }
    }
}
