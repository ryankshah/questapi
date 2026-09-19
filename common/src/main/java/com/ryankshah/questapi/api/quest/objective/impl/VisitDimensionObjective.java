package com.ryankshah.questapi.api.quest.objective.impl;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ryankshah.questapi.api.quest.QuestContext;
import com.ryankshah.questapi.api.quest.objective.ObjectiveDefinition;
import com.ryankshah.questapi.api.quest.objective.ObjectiveProgress;
import com.ryankshah.questapi.api.quest.objective.ObjectiveType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * Poll-based exploration objective satisfied once the player is present in a specific dimension.
 */
public final class VisitDimensionObjective implements ObjectiveDefinition {

    public static final Identifier TYPE_ID = Identifier.fromNamespaceAndPath("questapi", "visit_dimension");

    public static final MapCodec<VisitDimensionObjective> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("dimension").forGetter(o -> o.dimensionId)
    ).apply(instance, VisitDimensionObjective::new));

    public static final ObjectiveType<VisitDimensionObjective> TYPE = new ObjectiveType<>(TYPE_ID, CODEC);

    private final Identifier dimensionId;

    public VisitDimensionObjective(Identifier dimensionId) {
        this.dimensionId = dimensionId;
    }

    @Override
    public Identifier typeId() {
        return TYPE_ID;
    }

    @Override
    public Component describe() {
        return Component.translatable("questapi.objective.visit_dimension", Component.translatable(
                "dimension." + dimensionId.getNamespace() + "." + dimensionId.getPath().replace('/', '.')));
    }

    @Override
    public int targetAmount() {
        return 1;
    }

    @Override
    public ItemStack icon() {
        return new ItemStack(Items.COMPASS);
    }

    @Override
    public int evaluate(QuestContext context, ObjectiveProgress progress, Identifier eventKey, int eventValue) {
        ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, dimensionId);
        boolean present = context.player().level().dimension().equals(key);
        return present ? 1 : progress.current();
    }

    public Identifier dimensionId() {
        return dimensionId;
    }
}
