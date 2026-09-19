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
 * Poll-based objective tracking the vanilla {@code minecraft:animals_bred} custom statistic,
 * relative to a baseline captured when the quest became active. Counts any successful animal
 * breeding, matching what the vanilla statistics screen reports - not limited to a single species.
 */
public final class BreedAnimalsObjective implements ObjectiveDefinition {

    public static final Identifier TYPE_ID = Identifier.fromNamespaceAndPath("questapi", "breed_animals");

    public static final MapCodec<BreedAnimalsObjective> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("amount").forGetter(o -> o.amount)
    ).apply(instance, BreedAnimalsObjective::new));

    public static final ObjectiveType<BreedAnimalsObjective> TYPE = new ObjectiveType<>(TYPE_ID, CODEC);

    private final int amount;

    public BreedAnimalsObjective(int amount) {
        this.amount = amount;
    }

    @Override
    public Identifier typeId() {
        return TYPE_ID;
    }

    @Override
    public Component describe() {
        return Component.translatable("questapi.objective.breed_animals", amount);
    }

    @Override
    public int targetAmount() {
        return amount;
    }

    @Override
    public ItemStack icon() {
        return new ItemStack(Items.WHEAT);
    }

    @Override
    public int evaluate(QuestContext context, ObjectiveProgress progress, Identifier eventKey, int eventValue) {
        int total = context.player().getStats().getValue(Stats.CUSTOM.get(Stats.ANIMALS_BRED));
        return Math.max(0, total - progress.baseline());
    }

    @Override
    public int captureBaseline(QuestContext context) {
        return context.player().getStats().getValue(Stats.CUSTOM.get(Stats.ANIMALS_BRED));
    }
}
