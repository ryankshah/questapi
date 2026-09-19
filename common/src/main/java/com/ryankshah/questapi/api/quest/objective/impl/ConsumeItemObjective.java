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
 * Poll-based objective tracking the vanilla {@link Stats#ITEM_USED} statistic for a specific item,
 * relative to a baseline captured when the quest became active. Vanilla increments this stat whenever
 * an item finishes its "use" action, which covers eating food, drinking potions, using a bucket, and
 * similar item-triggered consumption alike - it is not limited to food specifically.
 */
public final class ConsumeItemObjective implements ObjectiveDefinition {

    public static final Identifier TYPE_ID = Identifier.fromNamespaceAndPath("questapi", "consume_item");

    public static final MapCodec<ConsumeItemObjective> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(o -> o.item),
            Codec.INT.fieldOf("amount").forGetter(o -> o.amount)
    ).apply(instance, ConsumeItemObjective::new));

    public static final ObjectiveType<ConsumeItemObjective> TYPE = new ObjectiveType<>(TYPE_ID, CODEC);

    private final Item item;
    private final int amount;

    public ConsumeItemObjective(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    @Override
    public Identifier typeId() {
        return TYPE_ID;
    }

    @Override
    public Component describe() {
        return Component.translatable("questapi.objective.consume_item", amount, new ItemStack(item).getHoverName());
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
        int total = context.player().getStats().getValue(Stats.ITEM_USED.get(item));
        return Math.max(0, total - progress.baseline());
    }

    @Override
    public int captureBaseline(QuestContext context) {
        return context.player().getStats().getValue(Stats.ITEM_USED.get(item));
    }

    public Item item() {
        return item;
    }
}
