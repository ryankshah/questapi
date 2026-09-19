package com.ryankshah.questapi.api.quest.reward;

import com.ryankshah.questapi.api.quest.QuestContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;

/**
 * Static description of something granted to a player when they claim a completed quest.
 * <p>
 * Reward instances are immutable and hold no claim-state; whether a specific player has already
 * claimed a quest's rewards is tracked separately by {@code QuestProgress#rewardClaimed()} so that
 * claiming is idempotent even across server restarts.
 */
public interface QuestReward {

    Identifier typeId();

    /**
     * Human-readable description shown in the rewards panel, e.g. "5x Iron Ingot".
     */
    Component describe();

    /**
     * Icon rendered next to this reward in the default GUI.
     */
    ItemStack icon();

    /**
     * Grants this reward to the player. Always called server-side, and always exactly once per
     * quest completion thanks to the {@code rewardClaimed} guard in {@code QuestManager}.
     */
    void grant(QuestContext context);
}
