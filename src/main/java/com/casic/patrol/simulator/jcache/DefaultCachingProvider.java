package com.casic.patrol.simulator.jcache;

import javax.cache.CacheManager;
import javax.cache.configuration.OptionalFeature;
import javax.cache.spi.CachingProvider;
import java.net.URI;
import java.util.Properties;

public class DefaultCachingProvider implements CachingProvider {
    private CacheManager cacheManager = new DefaultCacheManager(this);

    public CacheManager getCacheManager(URI uri, ClassLoader classLoader,
            Properties properties) {
        return cacheManager;
    }

    public ClassLoader getDefaultClassLoader() {
        return null;
    }

    public URI getDefaultURI() {
        return null;
    }

    public Properties getDefaultProperties() {
        return null;
    }

    public CacheManager getCacheManager(URI uri, ClassLoader classLoader) {
        return cacheManager;
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void close() {
    }

    public void close(ClassLoader classLoader) {
    }

    public void close(URI uri, ClassLoader classLoader) {
    }

    public boolean isSupported(OptionalFeature optionalFeature) {
        return false;
    }
}
