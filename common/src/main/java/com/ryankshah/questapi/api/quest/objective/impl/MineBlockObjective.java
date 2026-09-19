package com.ryankshah.questapi.api.quest.objective.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ryankshah.questapi.api.quest.QuestContext;
import com.ryankshah.questapi.api.quest.objective.ObjectiveDefinition;
import com.ryankshah.questapi.api.quest.objective.ObjectiveProgress;
import com.ryankshah.questapi.api.quest.objective.ObjectiveType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.Stats;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

/**
 * Poll-based objective tracking the vanilla {@link Stats#BLOCK_MINED} statistic for a specific block,
 * relative to a baseline captured when the quest became active so that blocks mined before starting
 * the quest do not count.
 */
public final class MineBlockObjective implements ObjectiveDefinition {

    public static final Identifier TYPE_ID = Identifier.fromNamespaceAndPath("questapi", "mine_block");

    public static final MapCodec<MineBlockObjective> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(o -> o.block),
            Codec.INT.fieldOf("amount").forGetter(o -> o.amount)
    ).apply(instance, MineBlockObjective::new));

    public static final ObjectiveType<MineBlockObjective> TYPE = new ObjectiveType<>(TYPE_ID, CODEC);

    private final Block block;
    private final int amount;

    public MineBlockObjective(Block block, int amount) {
        this.block = block;
        this.amount = amount;
    }

    @Override
    public Identifier typeId() {
        return TYPE_ID;
    }

    @Override
    public Component describe() {
        return Component.translatable("questapi.objective.mine_block", amount, block.getName());
    }

    @Override
    public int targetAmount() {
        return amount;
    }

    @Override
    public ItemStack icon() {
        return new ItemStack(block.asItem());
    }

    @Override
    public int evaluate(QuestContext context, ObjectiveProgress progress, Identifier eventKey, int eventValue) {
        int total = context.player().getStats().getValue(Stats.BLOCK_MINED.get(block));
        return Math.max(0, total - progress.baseline());
    }

    @Override
    public int captureBaseline(QuestContext context) {
        return context.player().getStats().getValue(Stats.BLOCK_MINED.get(block));
    }

    public Block block() {
        return block;
    }
}
