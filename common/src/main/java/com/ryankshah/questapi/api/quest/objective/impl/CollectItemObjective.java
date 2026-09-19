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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Poll-based objective satisfied while the player currently possesses at least {@code targetAmount}
 * of a given item, summed across their entire inventory (main, offhand and armor slots).
 * <p>
 * Because this checks current possession rather than a cumulative pickup count, progress can go
 * back down if the player consumes, drops or trades away the item away before the quest is claimed.
 */
public final class CollectItemObjective implements ObjectiveDefinition {

    public static final Identifier TYPE_ID = Identifier.fromNamespaceAndPath("questapi", "collect_item");

    public static final MapCodec<CollectItemObjective> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(o -> o.item),
            Codec.INT.fieldOf("amount").forGetter(o -> o.amount)
    ).apply(instance, CollectItemObjective::new));

    public static final ObjectiveType<CollectItemObjective> TYPE = new ObjectiveType<>(TYPE_ID, CODEC);

    private final Item item;
    private final int amount;

    public CollectItemObjective(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    @Override
    public Identifier typeId() {
        return TYPE_ID;
    }

    @Override
    public Component describe() {
        return Component.translatable("questapi.objective.collect_item", amount, new ItemStack(item).getHoverName());
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
        return context.player().getInventory().countItem(item);
    }

    public Item item() {
        return item;
    }
}
