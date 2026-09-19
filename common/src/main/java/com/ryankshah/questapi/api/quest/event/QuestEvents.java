package com.ryankshah.questapi.api.quest.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Minimal, loader-independent event bus for {@link QuestEventListener}s. Third-party mods register
 * a listener once during initialization; the Questing API invokes every registered listener for
 * every relevant state change.
 */
public final class QuestEvents {

    private static final List<QuestEventListener> LISTENERS = new CopyOnWriteArrayList<>();

    private QuestEvents() {
    }

    public static void register(QuestEventListener listener) {
        LISTENERS.add(listener);
    }

    public static List<QuestEventListener> listeners() {
        return LISTENERS;
    }
}
