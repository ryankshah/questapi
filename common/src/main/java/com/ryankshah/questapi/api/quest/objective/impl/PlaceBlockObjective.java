package com.ryankshah.questapi.api.quest.objective.impl;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.ryankshah.questapi.api.quest.QuestContext;
import com.ryankshah.questapi.api.quest.objective.ObjectiveDefinition;
import com.ryankshah.questapi.api.quest.objective.ObjectiveEventKeys;
import com.ryankshah.questapi.api.quest.objective.ObjectiveProgress;
import com.ryankshah.questapi.api.quest.objective.ObjectiveType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

/**
 * Push-based objective counting blocks placed by the player. There is no vanilla statistic or
 * cross-loader event for block placement, so this is driven by
 * {@code com.ryankshah.questapi.mixin.ServerPlayerGameModeMixin} in the common module, which injects
 * into {@code ServerPlayerGameMode#useItemOn} and reports successful placements identically on both
 * Fabric and NeoForge.
 */
public final class PlaceBlockObjective implements ObjectiveDefinition {

    public static final Identifier TYPE_ID = Identifier.fromNamespaceAndPath("questapi", "place_block");

    public static final MapCodec<PlaceBlockObjective> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.BLOCK.byNameCodec().fieldOf("block").forGetter(o -> o.block),
            Codec.INT.fieldOf("amount").forGetter(o -> o.amount)
    ).apply(instance, PlaceBlockObjective::new));

    public static final ObjectiveType<PlaceBlockObjective> TYPE = new ObjectiveType<>(TYPE_ID, CODEC);

    private final Block block;
    private final int amount;

    public PlaceBlockObjective(Block block, int amount) {
        this.block = block;
        this.amount = amount;
    }

    @Override
    public Identifier typeId() {
        return TYPE_ID;
    }

    @Override
    public Component describe() {
        return Component.translatable("questapi.objective.place_block", amount, block.getName());
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
        if (eventKey.equals(ObjectiveEventKeys.BLOCK_PLACED) && eventValue == blockId()) {
            return progress.current() + 1;
        }
        return progress.current();
    }

    private int blockId() {
        return BuiltInRegistries.BLOCK.getId(block);
    }

    public Block block() {
        return block;
    }
}
