package com.ryankshah.questapi.api.quest.objective;

import com.mojang.serialization.MapCodec;
import net.minecraft.resources.Identifier;

/**
 * A registered kind of objective, pairing its identifier with the {@link MapCodec} used to
 * (de)serialize definitions of this type, e.g. for datapack-driven quests or network sync.
 * <p>
 * Third-party mods register their own {@code ObjectiveType} instances via
 * {@link com.ryankshah.questapi.api.QuestRegistry#registerObjectiveType} during their common
 * initialization phase, without needing to modify this module.
 *
 * @param <D> the concrete {@link ObjectiveDefinition} implementation this type produces
 */
public record ObjectiveType<D extends ObjectiveDefinition>(Identifier id, MapCodec<D> codec) {
}
