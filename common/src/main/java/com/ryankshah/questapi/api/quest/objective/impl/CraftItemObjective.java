package com.ryankshah.questapi.api.quest.objective.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ryankshah.questapi.api.quest.QuestContext;
import com.ryankshah.questapi.api.quest.objective.ObjectiveDefinition;
import com.ryankshah.questapi.api.quest.objective.ObjectiveProgress;
import com.ryankshah.questapi.api.quest.objective.ObjectiveType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Poll-based objective tracking the vanilla {@link Stats#ITEM_CRAFTED} statistic for a specific item,
 * relative to a baseline captured when the quest became active.
 */
public final class CraftItemObjective implements ObjectiveDefinition {

    public static final Identifier TYPE_ID = Identifier.fromNamespaceAndPath("questapi", "craft_item");

    public static final MapCodec<CraftItemObjective> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(o -> o.item),
            Codec.INT.fieldOf("amount").forGetter(o -> o.amount)
    ).apply(instance, CraftItemObjective::new));

    public static final ObjectiveType<CraftItemObjective> TYPE = new ObjectiveType<>(TYPE_ID, CODEC);

    private final Item item;
    private final int amount;

    public CraftItemObjective(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    @Override
    public Identifier typeId() {
        return TYPE_ID;
    }

    @Override
    public Component describe() {
        return Component.translatable("questapi.objective.craft_item", amount, new ItemStack(item).getHoverName());
    }

    @Override
    public int targetAmount() {
        return amount;
    }

    @Override
    public ItemStack icon() {
        return new ItemStack(item);
    }

    @Override
    public int evaluate(QuestContext context, ObjectiveProgress progress, Identifier eventKey, int eventValue) {
        int total = context.player().getStats().getValue(Stats.ITEM_CRAFTED.get(item));
        return Math.max(0, total - progress.baseline());
    }

    @Override
    public int captureBaseline(QuestContext context) {
        return context.player().getStats().getValue(Stats.ITEM_CRAFTED.get(item));
    }

    public Item item() {
        return item;
    }
}
