package com.ryankshah.questapi;

import com.mojang.blaze3d.platform.InputConstants;
import com.ryankshah.questapi.client.gui.QuestScreen;
import com.ryankshah.questapi.client.network.ClientQuestNetworking;
import com.ryankshah.questapi.impl.network.payload.ClientboundSyncDefinitionsPayload;
import com.ryankshah.questapi.impl.network.payload.ClientboundSyncProgressPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

public class QuestApiFabricClient implements ClientModInitializer {

    private static KeyMapping openQuestsKey;

    @Override
    public void onInitializeClient() {
        openQuestsKey = KeyMappingHelper.registerKeyMapping(new KeyMapping(
                "key.questapi.open_quests", InputConstants.Type.KEYBOARD, InputConstants.KEY_K, KeyMapping.Category.MISC));

        ClientPlayNetworking.registerGlobalReceiver(ClientboundSyncDefinitionsPayload.TYPE,
                (payload, context) -> ClientQuestNetworking.handleSyncDefinitions(payload.categories(), payload.quests()));
        ClientPlayNetworking.registerGlobalReceiver(ClientboundSyncProgressPayload.TYPE,
                (payload, context) -> ClientQuestNetworking.handleSyncProgress(payload.data()));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openQuestsKey.consumeClick()) {
                if (client.gui.screen() == null) {
                    QuestScreen.open();
                }
            }
        });
    }
}
