package com.ryankshah.questapi.impl;

import com.ryankshah.questapi.QuestApi;
import com.ryankshah.questapi.platform.Services;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

/**
 * Smallest-sensible development configuration mechanism, shared by both loaders.
 * <p>
 * Reads (and, on first run, creates) {@code config/questapi.properties} with a single {@code dev}
 * flag. When {@code true}, the example quest tree and the {@code /quests} debug commands are
 * registered; production quest content registered by other mods is completely unaffected either way.
 */
public final class DevConfig {

    private static boolean devMode = false;
    private static boolean loaded = false;

    private DevConfig() {
    }

    public static boolean isDevMode() {
        return devMode;
    }

    public static void load() {
        if (loaded) {
            return;
        }
        loaded = true;
        Path file = Services.PLATFORM.getConfigDirectory().resolve("questapi.properties");
        Properties props = new Properties();
        if (Files.exists(file)) {
            try (InputStream in = Files.newInputStream(file)) {
                props.load(in);
            } catch (IOException e) {
                QuestApi.LOG.warn("Failed to read questapi.properties, defaulting to dev=false", e);
            }
        } else {
            props.setProperty("dev", "false");
            try {
                Files.createDirectories(file.getParent());
            } catch (IOException ignored) {
            }
            try (OutputStream out = Files.newOutputStream(file)) {
                props.store(out, "QuestAPI configuration. Set dev=true to register the example quest tree and /quests debug commands.");
            } catch (IOException e) {
                QuestApi.LOG.warn("Failed to write default questapi.properties", e);
            }
        }
        devMode = Boolean.parseBoolean(props.getProperty("dev", "false"));
        QuestApi.LOG.info("QuestAPI dev mode: {}", devMode);
    }
}
