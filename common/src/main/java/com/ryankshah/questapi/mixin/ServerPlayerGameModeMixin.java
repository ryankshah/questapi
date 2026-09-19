package com.ryankshah.questapi.mixin;

import com.ryankshah.questapi.QuestApi;
import com.ryankshah.questapi.api.quest.objective.ObjectiveEventKeys;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * There is no vanilla statistic or cross-loader event fired specifically when a player places a
 * block (unlike mining, which increments {@link net.minecraft.stats.Stats#BLOCK_MINED}
 * automatically), so {@code PlaceBlockObjective} is driven from here instead. Injecting into this
 * common, non-obfuscated-per-loader method means the behaviour is identical on Fabric and NeoForge
 * without needing two separate loader-specific hooks.
 */
@Mixin(ServerPlayerGameMode.class)
public abstract class ServerPlayerGameModeMixin {

    @Inject(method = "useItemOn", at = @At("RETURN"))
    private void questapi$onUseItemOn(ServerPlayer player, Level level, ItemStack itemStack, InteractionHand hand,
                                       BlockHitResult hitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (cir.getReturnValue().consumesAction() && itemStack.getItem() instanceof BlockItem blockItem) {
            int blockId = BuiltInRegistries.BLOCK.getId(blockItem.getBlock());
            QuestApi.manager().pushObjectiveEvent(player, ObjectiveEventKeys.BLOCK_PLACED, blockId);
        }
    }
}
