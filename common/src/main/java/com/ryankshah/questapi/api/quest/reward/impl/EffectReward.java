package com.ryankshah.questapi.api.quest.reward.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ryankshah.questapi.api.quest.QuestContext;
import com.ryankshah.questapi.api.quest.reward.QuestReward;
import com.ryankshah.questapi.api.quest.reward.RewardType;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Applies a potion/mob effect to the player.
 */
public final class EffectReward implements QuestReward {

    public static final Identifier TYPE_ID = Identifier.fromNamespaceAndPath("questapi", "effect");

    public static final MapCodec<EffectReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.MOB_EFFECT.holderByNameCodec().fieldOf("effect").forGetter(o -> o.effect),
            Codec.INT.fieldOf("duration_ticks").forGetter(o -> o.durationTicks),
            Codec.INT.optionalFieldOf("amplifier", 0).forGetter(o -> o.amplifier)
    ).apply(instance, EffectReward::new));

    public static final RewardType<EffectReward> TYPE = new RewardType<>(TYPE_ID, CODEC);

    private final Holder<MobEffect> effect;
    private final int durationTicks;
    private final int amplifier;

    public EffectReward(Holder<MobEffect> effect, int durationTicks, int amplifier) {
        this.effect = effect;
        this.durationTicks = durationTicks;
        this.amplifier = amplifier;
    }

    @Override
    public Identifier typeId() {
        return TYPE_ID;
    }

    @Override
    public Component describe() {
        return Component.translatable("questapi.reward.effect", effect.value().getDisplayName(), durationTicks / 20);
    }

    @Override
    public ItemStack icon() {
        return new ItemStack(Items.POTION);
    }

    @Override
    public void grant(QuestContext context) {
        context.player().addEffect(new MobEffectInstance(effect, durationTicks, amplifier));
    }
}
