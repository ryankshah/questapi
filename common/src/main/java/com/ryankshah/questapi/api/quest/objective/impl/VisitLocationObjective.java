package com.ryankshah.questapi.api.quest.objective.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ryankshah.questapi.api.quest.QuestContext;
import com.ryankshah.questapi.api.quest.objective.ObjectiveDefinition;
import com.ryankshah.questapi.api.quest.objective.ObjectiveProgress;
import com.ryankshah.questapi.api.quest.objective.ObjectiveType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * Poll-based exploration objective satisfied once the player comes within {@code radius} blocks of a
 * target position in a specific dimension.
 */
public final class VisitLocationObjective implements ObjectiveDefinition {

    public static final Identifier TYPE_ID = Identifier.fromNamespaceAndPath("questapi", "visit_location");

    public static final MapCodec<VisitLocationObjective> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("dimension").forGetter(o -> o.dimensionId),
            BlockPos.CODEC.fieldOf("pos").forGetter(o -> o.pos),
            Codec.INT.fieldOf("radius").forGetter(o -> o.radius),
            ComponentSerialization.CODEC.optionalFieldOf("name", Component.literal("")).forGetter(o -> o.locationName)
    ).apply(instance, VisitLocationObjective::new));

    public static final ObjectiveType<VisitLocationObjective> TYPE = new ObjectiveType<>(TYPE_ID, CODEC);

    private final Identifier dimensionId;
    private final BlockPos pos;
    private final int radius;
    private final Component locationName;

    public VisitLocationObjective(Identifier dimensionId, BlockPos pos, int radius, Component locationName) {
        this.dimensionId = dimensionId;
        this.pos = pos;
        this.radius = radius;
        this.locationName = locationName;
    }

    @Override
    public Identifier typeId() {
        return TYPE_ID;
    }

    @Override
    public Component describe() {
        Component target = locationName.getString().isEmpty()
                ? Component.literal(pos.getX() + ", " + pos.getY() + ", " + pos.getZ())
                : locationName;
        return Component.translatable("questapi.objective.visit_location", target);
    }

    @Override
    public int targetAmount() {
        return 1;
    }

    @Override
    public ItemStack icon() {
        return new ItemStack(Items.MAP);
    }

    @Override
    public int evaluate(QuestContext context, ObjectiveProgress progress, Identifier eventKey, int eventValue) {
        ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, dimensionId);
        if (!context.player().level().dimension().equals(key)) {
            return progress.current();
        }
        double distSq = context.player().blockPosition().distSqr(pos);
        boolean within = distSq <= (double) radius * radius;
        return within ? 1 : progress.current();
    }
}
