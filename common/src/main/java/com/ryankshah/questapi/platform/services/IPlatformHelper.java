package com.ryankshah.questapi.platform.services;

import java.nio.file.Path;

public interface IPlatformHelper {

    /**
     * The loader's shared configuration directory (e.g. {@code run/config}).
     */
    Path getConfigDirectory();

    /**
     * Gets the name of the current platform.
     */
    String getPlatformName();

    /**
     * Checks if a mod with the given id is loaded.
     */
    boolean isModLoaded(String modId);

    /**
     * Check if the game is currently in a development environment.
     */
    boolean isDevelopmentEnvironment();

    default String getEnvironmentName() {
        return isDevelopmentEnvironment() ? "development" : "production";
    }
}
