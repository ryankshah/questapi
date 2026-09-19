package com.ryankshah.questapi.platform;

import com.ryankshah.questapi.QuestApi;
import com.ryankshah.questapi.platform.services.INetworkHelper;
import com.ryankshah.questapi.platform.services.IPlatformHelper;

import java.util.ServiceLoader;

/**
 * Locates the platform-specific implementation of each service interface via Java's
 * {@link ServiceLoader}. Each loader module provides its implementation through a
 * {@code META-INF/services} entry.
 */
public final class Services {

    public static final IPlatformHelper PLATFORM = load(IPlatformHelper.class);
    public static final INetworkHelper NETWORK = load(INetworkHelper.class);

    private Services() {
    }

    public static <T> T load(Class<T> clazz) {
        final T loadedService = ServiceLoader.load(clazz, Services.class.getClassLoader())
                .findFirst()
                .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        QuestApi.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
