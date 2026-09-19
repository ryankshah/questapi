package com.ryankshah.questapi.api.quest.objective;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

/**
 * Mutable, per-player runtime progress for a single objective within an active {@code QuestProgress}.
 * <p>
 * This is intentionally decoupled from {@link ObjectiveDefinition}: the definition never changes once
 * registered, while instances of this class are created fresh per-player and persisted to disk.
 */
public final class ObjectiveProgress {

    public static final Codec<ObjectiveProgress> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("current").forGetter(ObjectiveProgress::current),
            Codec.INT.fieldOf("baseline").forGetter(ObjectiveProgress::baseline),
            Codec.BOOL.fieldOf("complete").forGetter(ObjectiveProgress::complete)
    ).apply(instance, ObjectiveProgress::new));

    private int current;
    private int baseline;
    private boolean complete;

    public ObjectiveProgress(int current, int baseline, boolean complete) {
        this.current = current;
        this.baseline = baseline;
        this.complete = complete;
    }

    public static ObjectiveProgress empty() {
        return new ObjectiveProgress(0, 0, false);
    }

    public int current() {
        return current;
    }

    public int baseline() {
        return baseline;
    }

    public boolean complete() {
        return complete;
    }

    public void setBaseline(int baseline) {
        this.baseline = baseline;
    }

    /**
     * Updates the current progress amount, clamping to the objective's target and recomputing the
     * completion flag. Returns {@code true} if this call caused the objective to become newly complete.
     */
    public boolean updateCurrent(int newAmount, int target) {
        boolean wasComplete = complete;
        this.current = Math.max(0, Math.min(newAmount, target));
        this.complete = this.current >= target;
        return this.complete && !wasComplete;
    }

    public ObjectiveProgress copy() {
        return new ObjectiveProgress(current, baseline, complete);
    }
}
