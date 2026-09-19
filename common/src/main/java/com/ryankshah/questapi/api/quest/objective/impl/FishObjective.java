package com.ryankshah.questapi.api.quest.objective.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ryankshah.questapi.api.quest.QuestContext;
import com.ryankshah.questapi.api.quest.objective.ObjectiveDefinition;
import com.ryankshah.questapi.api.quest.objective.ObjectiveProgress;
import com.ryankshah.questapi.api.quest.objective.ObjectiveType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Poll-based objective tracking the vanilla {@code minecraft:fish_caught} custom statistic,
 * relative to a baseline captured when the quest became active. Counts any fish caught on a
 * fishing rod, matching what the vanilla statistics screen reports.
 */
public final class FishObjective implements ObjectiveDefinition {

    public static final Identifier TYPE_ID = Identifier.fromNamespaceAndPath("questapi", "fish");

    public static final MapCodec<FishObjective> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("amount").forGetter(o -> o.amount)
    ).apply(instance, FishObjective::new));

    public static final ObjectiveType<FishObjective> TYPE = new ObjectiveType<>(TYPE_ID, CODEC);

    private final int amount;

    public FishObjective(int amount) {
        this.amount = amount;
    }

    @Override
    public Identifier typeId() {
        return TYPE_ID;
    }

    @Override
    public Component describe() {
        return Component.translatable("questapi.objective.fish", amount);
    }

    @Override
    public int targetAmount() {
        return amount;
    }

    @Override
    public ItemStack icon() {
        return new ItemStack(Items.FISHING_ROD);
    }

    @Override
    public int evaluate(QuestContext context, ObjectiveProgress progress, Identifier eventKey, int eventValue) {
        int total = context.player().getStats().getValue(Stats.CUSTOM.get(Stats.FISH_CAUGHT));
        return Math.max(0, total - progress.baseline());
    }

    @Override
    public int captureBaseline(QuestContext context) {
        return context.player().getStats().getValue(Stats.CUSTOM.get(Stats.FISH_CAUGHT));
    }
}
