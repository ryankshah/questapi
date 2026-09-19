package com.ryankshah.questapi.api.quest.objective.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ryankshah.questapi.api.quest.QuestContext;
import com.ryankshah.questapi.api.quest.objective.ObjectiveDefinition;
import com.ryankshah.questapi.api.quest.objective.ObjectiveEventKeys;
import com.ryankshah.questapi.api.quest.objective.ObjectiveProgress;
import com.ryankshah.questapi.api.quest.objective.ObjectiveType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Push-based objective completed by taming any number of animals. There is no cross-loader vanilla
 * event for taming, so this is driven by {@code com.ryankshah.questapi.mixin.AnimalMixin} in the
 * common module, which injects into {@code Animal#tame} identically on both Fabric and NeoForge.
 */
public final class TameEntityObjective implements ObjectiveDefinition {

    public static final Identifier TYPE_ID = Identifier.fromNamespaceAndPath("questapi", "tame_entity");

    public static final MapCodec<TameEntityObjective> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("amount").forGetter(o -> o.amount)
    ).apply(instance, TameEntityObjective::new));

    public static final ObjectiveType<TameEntityObjective> TYPE = new ObjectiveType<>(TYPE_ID, CODEC);

    private final int amount;

    public TameEntityObjective(int amount) {
        this.amount = amount;
    }

    @Override
    public Identifier typeId() {
        return TYPE_ID;
    }

    @Override
    public Component describe() {
        return Component.translatable("questapi.objective.tame_entity", amount);
    }

    @Override
    public int targetAmount() {
        return amount;
    }

    @Override
    public ItemStack icon() {
        return new ItemStack(Items.LEAD);
    }

    @Override
    public int evaluate(QuestContext context, ObjectiveProgress progress, Identifier eventKey, int eventValue) {
        if (eventKey.equals(ObjectiveEventKeys.ENTITY_TAMED)) {
            return progress.current() + 1;
        }
        return progress.current();
    }
}
