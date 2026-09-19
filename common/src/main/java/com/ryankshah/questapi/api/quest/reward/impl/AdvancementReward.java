package com.ryankshah.questapi.api.quest.reward.impl;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ryankshah.questapi.api.quest.QuestContext;
import com.ryankshah.questapi.api.quest.reward.QuestReward;
import com.ryankshah.questapi.api.quest.reward.RewardType;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Grants a specific vanilla or datapack advancement to the player, by awarding every one of its
 * criteria - a single {@link net.minecraft.server.PlayerAdvancements#award} call only satisfies one
 * criterion, so an advancement with several needs all of them awarded to actually complete.
 */
public final class AdvancementReward implements QuestReward {

    public static final Identifier TYPE_ID = Identifier.fromNamespaceAndPath("questapi", "advancement");

    public static final MapCodec<AdvancementReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("advancement").forGetter(o -> o.advancementId)
    ).apply(instance, AdvancementReward::new));

    public static final RewardType<AdvancementReward> TYPE = new RewardType<>(TYPE_ID, CODEC);

    private final Identifier advancementId;

    public AdvancementReward(Identifier advancementId) {
        this.advancementId = advancementId;
    }

    @Override
    public Identifier typeId() {
        return TYPE_ID;
    }

    @Override
    public Component describe() {
        return Component.translatable("questapi.reward.advancement", advancementId.toString());
    }

    @Override
    public ItemStack icon() {
        return new ItemStack(Items.WRITTEN_BOOK);
    }

    @Override
    public void grant(QuestContext context) {
        AdvancementHolder advancement = context.server().getAdvancements().get(advancementId);
        if (advancement == null) {
            return;
        }
        for (String criterion : advancement.value().criteria().keySet()) {
            context.player().getAdvancements().award(advancement, criterion);
        }
    }

    public Identifier advancementId() {
        return advancementId;
    }
}
