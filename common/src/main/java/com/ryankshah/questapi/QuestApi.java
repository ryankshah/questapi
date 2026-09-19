package com.ryankshah.questapi;

import com.ryankshah.questapi.api.quest.event.QuestEvents;
import com.ryankshah.questapi.impl.BuiltinContent;
import com.ryankshah.questapi.impl.QuestManagerImpl;
import com.ryankshah.questapi.impl.QuestRegistryImpl;
import com.ryankshah.questapi.api.QuestManager;
import com.ryankshah.questapi.api.QuestRegistry;
import com.ryankshah.questapi.impl.network.QuestSyncListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Static entry point holding mod-wide constants and the shared registry/manager singletons.
 * <p>
 * This class is loader-agnostic. Fabric and NeoForge entrypoints both call into {@link #bootstrap()}
 * during their respective mod initialization phase.
 */
public final class QuestApi {

    public static final String MOD_ID = "questapi";
    public static final String MOD_NAME = "QuestAPI";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

    private static final QuestRegistryImpl REGISTRY = new QuestRegistryImpl();
    private static final QuestManagerImpl MANAGER = new QuestManagerImpl(REGISTRY);

    private QuestApi() {
    }

    /**
     * The registry of static quest/objective/reward/condition type definitions.
     */
    public static QuestRegistry registry() {
        return REGISTRY;
    }

    /**
     * The server-authoritative runtime manager responsible for player quest progress.
     */
    public static QuestManager manager() {
        return MANAGER;
    }

    private static boolean bootstrapped = false;

    /**
     * Called once by each loader's common init hook, before any mod registers its own quests.
     * Registers the built-in objective/reward/condition types and wires up network sync.
     */
    public static void bootstrap() {
        if (bootstrapped) {
            return;
        }
        bootstrapped = true;
        LOG.info("Initializing {} common core", MOD_NAME);
        BuiltinContent.registerAll(REGISTRY);
        QuestEvents.register(new QuestSyncListener());
    }
}
