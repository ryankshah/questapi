package com.ryankshah.questapi.mixin;

import com.ryankshah.questapi.QuestApi;
import com.ryankshah.questapi.api.quest.objective.ObjectiveEventKeys;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * There is no cross-loader vanilla event fired when a player tames an animal, so
 * {@code TameEntityObjective} is driven from here instead, identically on Fabric and NeoForge.
 * <p>
 * Only covers {@link TamableAnimal} subclasses (wolves, cats, parrots, ...); horses and other
 * mounts use a separate vanilla taming pathway not covered by this hook.
 */
@Mixin(TamableAnimal.class)
public abstract class TamableAnimalMixin {

    @Inject(method = "tame", at = @At("TAIL"))
    private void questapi$onTame(Player player, CallbackInfo ci) {
        if (player instanceof ServerPlayer serverPlayer) {
            QuestApi.manager().pushObjectiveEvent(serverPlayer, ObjectiveEventKeys.ENTITY_TAMED, 1);
        }
    }
}
