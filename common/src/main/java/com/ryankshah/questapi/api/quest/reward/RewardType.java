package com.ryankshah.questapi.api.quest.reward;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;

/**
 * A registered kind of reward, pairing its identifier with the {@link MapCodec} used to
 * (de)serialize instances of this type.
 * <p>
 * Third-party mods register their own {@code RewardType}s via
 * {@link com.ryankshah.questapi.api.QuestRegistry#registerRewardType} to grant custom currencies,
 * skill points, or anything else a quest should be able to hand out.
 *
 * @param <R> the concrete {@link QuestReward} implementation this type produces
 */
public record RewardType<R extends QuestReward>(Identifier id, MapCodec<R> codec) {
}
