package com.ryankshah.questapi.api.quest.reward.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ryankshah.questapi.api.quest.QuestContext;
import com.ryankshah.questapi.api.quest.reward.QuestReward;
import com.ryankshah.questapi.api.quest.reward.RewardType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.permissions.LevelBasedPermissionSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

/**
 * Executes a command as the rewarded player, e.g. {@code effect give @s minecraft:luck 6000 0}.
 * Runs with elevated permission and suppressed output so it behaves consistently regardless of the
 * player's own permission level, and never echoes command feedback into their chat.
 */
public final class CommandReward implements QuestReward {

    public static final Identifier TYPE_ID = Identifier.fromNamespaceAndPath("questapi", "command");

    public static final MapCodec<CommandReward> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.STRING.fieldOf("command").forGetter(o -> o.command)
    ).apply(instance, CommandReward::new));

    public static final RewardType<CommandReward> TYPE = new RewardType<>(TYPE_ID, CODEC);

    private final String command;

    public CommandReward(String command) {
        this.command = command;
    }

    @Override
    public Identifier typeId() {
        return TYPE_ID;
    }

    @Override
    public Component describe() {
        return Component.translatable("questapi.reward.command", command);
    }

    @Override
    public ItemStack icon() {
        return new ItemStack(Items.COMMAND_BLOCK);
    }

    @Override
    public void grant(QuestContext context) {
        CommandSourceStack source = context.player().createCommandSourceStack()
                .withPermission(LevelBasedPermissionSet.GAMEMASTER)
                .withSuppressedOutput();
        context.server().getCommands().performPrefixedCommand(source, command);
    }
}
