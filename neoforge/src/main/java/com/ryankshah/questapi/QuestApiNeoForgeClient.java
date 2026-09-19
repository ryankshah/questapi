package com.ryankshah.questapi;

import com.mojang.blaze3d.platform.InputConstants;
import com.ryankshah.questapi.QuestApi;
import com.ryankshah.questapi.client.gui.QuestScreen;
import com.ryankshah.questapi.client.gui.QuestTrackerHud;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;
import net.neoforged.neoforge.common.NeoForge;

/**
 * Client-only bootstrap: registers the "open quests" keybinding and the client payload receivers.
 * Only ever touched from {@link QuestApiNeoForge} behind an {@code FMLEnvironment.dist.isClient()}
 * guard, so this class - and the client-only Minecraft classes it references - is never loaded on a
 * dedicated server.
 */
final class QuestApiNeoForgeClient {

    private static KeyMapping openQuestsKey;

    private QuestApiNeoForgeClient() {
    }

    static void init(IEventBus modEventBus) {
        modEventBus.addListener(QuestApiNeoForgeClient::registerKeyMappings);
        modEventBus.addListener(QuestApiNeoForgeClient::registerGuiLayers);
        NeoForge.EVENT_BUS.addListener(QuestApiNeoForgeClient::onClientTick);
    }

    private static void registerKeyMappings(RegisterKeyMappingsEvent event) {
        openQuestsKey = new KeyMapping("key.questapi.open_quests", InputConstants.Type.KEYBOARD, InputConstants.KEY_K, KeyMapping.Category.MISC);
        event.register(openQuestsKey);
    }

    private static void registerGuiLayers(RegisterGuiLayersEvent event) {
        event.registerAbove(VanillaGuiLayers.BOSS_OVERLAY,
                Identifier.fromNamespaceAndPath(QuestApi.MOD_ID, "quest_tracker"), QuestTrackerHud::render);
    }

    private static void onClientTick(ClientTickEvent.Post event) {
        while (openQuestsKey != null && openQuestsKey.consumeClick()) {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft.gui.screen() == null) {
                QuestScreen.open();
            }
        }
    }
}
