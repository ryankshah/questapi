package com.ryankshah.questapi.api.quest.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;

/**
 * A registered kind of condition, pairing its identifier with the {@link MapCodec} used to
 * (de)serialize instances of this type.
 *
 * @param <C> the concrete {@link QuestCondition} implementation this type produces
 */
public record ConditionType<C extends QuestCondition>(Identifier id, MapCodec<C> codec) {
}
