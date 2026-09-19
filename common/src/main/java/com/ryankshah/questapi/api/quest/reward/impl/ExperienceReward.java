package com.ryankshah.questapi.api.quest.reward.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ryankshah.questapi.api.quest.QuestContext;
import com.ryankshah.questapi.api.quest.reward.QuestReward;
import com.ryankshah.questapi.api.quest.reward.RewardType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Grants experience points to the player.
 */
public final class ExperienceReward implements QuestReward {

    public static final Identifier TYPE_ID = Identifier.fromNamespaceAndPath("questapi", "experience");

    public static final MapCodec<ExperienceReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("points").forGetter(o -> o.points)
    ).apply(instance, ExperienceReward::new));

    public static final RewardType<ExperienceReward> TYPE = new RewardType<>(TYPE_ID, CODEC);

    private final int points;

    public ExperienceReward(int points) {
        this.points = points;
    }

    @Override
    public Identifier typeId() {
        return TYPE_ID;
    }

    @Override
    public Component describe() {
        return Component.translatable("questapi.reward.experience", points);
    }

    @Override
    public ItemStack icon() {
        return new ItemStack(Items.EXPERIENCE_BOTTLE);
    }

    @Override
    public void grant(QuestContext context) {
        context.player().giveExperiencePoints(points);
    }
}
