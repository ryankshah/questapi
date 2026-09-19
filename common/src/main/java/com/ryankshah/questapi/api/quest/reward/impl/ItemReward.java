package com.ryankshah.questapi.api.quest.reward.impl;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ryankshah.questapi.api.quest.QuestContext;
import com.ryankshah.questapi.api.quest.reward.QuestReward;
import com.ryankshah.questapi.api.quest.reward.RewardType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Prediction;
import net.minecraft.world.item.ItemStack;

/**
 * Grants a stack of items to the player, placed directly in their inventory or dropped at their feet
 * if the inventory is full.
 */
public final class ItemReward implements QuestReward {

    public static final Identifier TYPE_ID = Identifier.fromNamespaceAndPath("questapi", "item");

    public static final MapCodec<ItemReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ItemStack.CODEC.fieldOf("stack").forGetter(o -> o.stack)
    ).apply(instance, ItemReward::new));

    public static final RewardType<ItemReward> TYPE = new RewardType<>(TYPE_ID, CODEC);

    private final ItemStack stack;

    public ItemReward(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public Identifier typeId() {
        return TYPE_ID;
    }

    @Override
    public Component describe() {
        return Component.translatable("questapi.reward.item", stack.getCount(), stack.getHoverName());
    }

    @Override
    public ItemStack icon() {
        return stack;
    }

    @Override
    public void grant(QuestContext context) {
        ItemStack copy = stack.copy();
        if (!context.player().getInventory().add(copy)) {
            context.player().drop(copy, false, Prediction.SERVER_ONLY);
        }
    }
}
