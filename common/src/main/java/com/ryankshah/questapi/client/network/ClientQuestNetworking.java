package com.ryankshah.questapi.client.network;

import com.ryankshah.questapi.api.quest.PlayerQuestData;
import com.ryankshah.questapi.api.quest.Quest;
import com.ryankshah.questapi.api.quest.QuestCategory;
import com.ryankshah.questapi.client.ClientQuestDataCache;
import com.ryankshah.questapi.impl.network.payload.ServerboundAbandonQuestPayload;
import com.ryankshah.questapi.impl.network.payload.ServerboundClaimRewardPayload;
import com.ryankshah.questapi.impl.network.payload.ServerboundDeliverItemsPayload;
import com.ryankshah.questapi.impl.network.payload.ServerboundRequestSyncPayload;
import com.ryankshah.questapi.impl.network.payload.ServerboundStartQuestPayload;
import com.ryankshah.questapi.platform.Services;
import net.minecraft.resources.Identifier;

import java.util.List;

/**
 * Client-side packet handling logic and outgoing request helpers, shared by both loaders. Loader
 * modules register these methods as the handler bodies for their own client payload registration.
 */
public final class ClientQuestNetworking {

    private ClientQuestNetworking() {
    }

    public static void handleSyncDefinitions(List<QuestCategory> categories, List<Quest> quests) {
        ClientQuestDataCache.INSTANCE.setDefinitions(categories, quests);
    }

    public static void handleSyncProgress(PlayerQuestData data) {
        ClientQuestDataCache.INSTANCE.setProgress(data);
    }

    public static void requestSync() {
        Services.NETWORK.sendToServer(new ServerboundRequestSyncPayload());
    }

    public static void requestStartQuest(Identifier questId) {
        Services.NETWORK.sendToServer(new ServerboundStartQuestPayload(questId));
    }

    public static void requestAbandonQuest(Identifier questId) {
        Services.NETWORK.sendToServer(new ServerboundAbandonQuestPayload(questId));
    }

    public static void requestClaimReward(Identifier questId) {
        Services.NETWORK.sendToServer(new ServerboundClaimRewardPayload(questId));
    }

    public static void requestDeliverItems(Identifier questId, int objectiveIndex, int amount) {
        Services.NETWORK.sendToServer(new ServerboundDeliverItemsPayload(questId, objectiveIndex, amount));
    }
}
