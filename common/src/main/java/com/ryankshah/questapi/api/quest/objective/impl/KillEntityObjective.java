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
import net.minecraft.core.Holder;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;

import java.util.Optional;

/**
 * Poll-based objective tracking the vanilla {@link Stats#ENTITY_KILLED} statistic for a specific
 * entity type, relative to a baseline captured when the quest became active.
 */
public final class KillEntityObjective implements ObjectiveDefinition {

    public static final Identifier TYPE_ID = Identifier.fromNamespaceAndPath("questapi", "kill_entity");

    public static final MapCodec<KillEntityObjective> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(o -> o.entityType),
            Codec.INT.fieldOf("amount").forGetter(o -> o.amount)
    ).apply(instance, KillEntityObjective::new));

    public static final ObjectiveType<KillEntityObjective> TYPE = new ObjectiveType<>(TYPE_ID, CODEC);

    private final EntityType<?> entityType;
    private final int amount;

    public KillEntityObjective(EntityType<?> entityType, int amount) {
        this.entityType = entityType;
        this.amount = amount;
    }

    @Override
    public Identifier typeId() {
        return TYPE_ID;
    }

    @Override
    public Component describe() {
        return Component.translatable("questapi.objective.kill_entity", amount, entityType.getDescription());
    }

    @Override
    public int targetAmount() {
        return amount;
    }

    @Override
    public ItemStack icon() {
        Optional<Holder<net.minecraft.world.item.Item>> spawnEgg = SpawnEggItem.byId(entityType);
        return spawnEgg.map(holder -> new ItemStack(holder.value())).orElse(new ItemStack(Items.IRON_SWORD));
    }

    @Override
    public int evaluate(QuestContext context, ObjectiveProgress progress, Identifier eventKey, int eventValue) {
        int total = context.player().getStats().getValue(Stats.ENTITY_KILLED.get(entityType));
        return Math.max(0, total - progress.baseline());
    }

    @Override
    public int captureBaseline(QuestContext context) {
        return context.player().getStats().getValue(Stats.ENTITY_KILLED.get(entityType));
    }

    public EntityType<?> entityType() {
        return entityType;
    }
}
