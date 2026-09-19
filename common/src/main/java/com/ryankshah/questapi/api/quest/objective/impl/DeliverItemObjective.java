package com.ryankshah.questapi.api.quest.objective.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ryankshah.questapi.api.quest.QuestContext;
import com.ryankshah.questapi.api.quest.objective.ObjectiveDefinition;
import com.ryankshah.questapi.api.quest.objective.ObjectiveEventKeys;
import com.ryankshah.questapi.api.quest.objective.ObjectiveProgress;
import com.ryankshah.questapi.api.quest.objective.ObjectiveType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Push-based objective completed by explicitly delivering (surrendering) items to the quest via the
 * default GUI's "deliver" action or the {@code /quests deliver} command, rather than merely holding
 * them. Delivered items are removed from the player's inventory server-side and cannot be reclaimed.
 * <p>
 * Progress is cumulative and only ever driven by {@link ObjectiveEventKeys#ITEM_DELIVERED}; unlike
 * {@link CollectItemObjective} it never decreases just because the player's inventory changes.
 */
public final class DeliverItemObjective implements ObjectiveDefinition {

    public static final Identifier TYPE_ID = Identifier.fromNamespaceAndPath("questapi", "deliver_item");

    public static final MapCodec<DeliverItemObjective> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(o -> o.item),
            Codec.INT.fieldOf("amount").forGetter(o -> o.amount)
    ).apply(instance, DeliverItemObjective::new));

    public static final ObjectiveType<DeliverItemObjective> TYPE = new ObjectiveType<>(TYPE_ID, CODEC);

    private final Item item;
    private final int amount;

    public DeliverItemObjective(Item item, int amount) {
        this.item = item;
        this.amount = amount;
    }

    @Override
    public Identifier typeId() {
        return TYPE_ID;
    }

    @Override
    public Component describe() {
        return Component.translatable("questapi.objective.deliver_item", amount, new ItemStack(item).getHoverName());
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
        if (eventKey.equals(ObjectiveEventKeys.ITEM_DELIVERED)) {
            return progress.current() + eventValue;
        }
        return progress.current();
    }

    public Item item() {
        return item;
    }
}
