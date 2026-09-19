package com.ryankshah.questapi.impl;

import com.ryankshah.questapi.QuestApi;
import com.ryankshah.questapi.api.quest.ResetMode;
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
 * Reads (and, on first run, creates) {@code config/questapi.properties} with a {@code dev} flag and
 * a {@code repeatable-quest-default-reset-mode} setting. When {@code dev} is {@code true}, the
 * example quest tree and the {@code /quests} debug commands are registered; production quest
 * content registered by other mods is completely unaffected either way.
 */
public final class DevConfig {

    private static boolean devMode = false;
    private static ResetMode defaultResetMode = ResetMode.WALL_CLOCK;
    private static boolean loaded = false;

    private DevConfig() {
    }

    public static boolean isDevMode() {
        return devMode;
    }

    /**
     * The reset mode a repeatable quest resolves to when it does not specify its own via
     * {@code Quest.Builder#repeatable(ResetMode, int)}.
     */
    public static ResetMode defaultResetMode() {
        return defaultResetMode;
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
            props.setProperty("repeatable-quest-default-reset-mode", ResetMode.WALL_CLOCK.name());
            try {
                Files.createDirectories(file.getParent());
            } catch (IOException ignored) {
            }
            try (OutputStream out = Files.newOutputStream(file)) {
                props.store(out, "QuestAPI configuration.\n"
                        + "dev: set to true to register the example quest tree and /quests debug commands.\n"
                        + "repeatable-quest-default-reset-mode: WALL_CLOCK or IN_GAME_DAY, used by repeatable\n"
                        + "quests that don't specify their own reset mode.");
            } catch (IOException e) {
                QuestApi.LOG.warn("Failed to write default questapi.properties", e);
            }
        }
        devMode = Boolean.parseBoolean(props.getProperty("dev", "false"));
        try {
            defaultResetMode = ResetMode.valueOf(props.getProperty("repeatable-quest-default-reset-mode", "WALL_CLOCK").trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            QuestApi.LOG.warn("Invalid repeatable-quest-default-reset-mode in questapi.properties, defaulting to WALL_CLOCK", e);
            defaultResetMode = ResetMode.WALL_CLOCK;
        }
        QuestApi.LOG.info("QuestAPI dev mode: {}, default repeatable-quest reset mode: {}", devMode, defaultResetMode);
    }
}
