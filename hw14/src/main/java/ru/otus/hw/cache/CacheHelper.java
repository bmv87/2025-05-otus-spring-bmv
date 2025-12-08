package ru.otus.hw.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.jcache.JCacheCacheManager;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CacheHelper {

    public static final String AUTHORS = "authors";

    public static final String GENRES = "genres";

    public static final String BOOKS = "books";

    private final JCacheCacheManager cacheManager;

    public Long tryGetId(String cacheName, Long id) {
        return (Long) Objects.requireNonNull(cacheManager.getCacheManager())
                .getCache(cacheName)
                .get(id);
    }

    public boolean putId(String cacheName, Long inId, Long outId) {
        return Objects.requireNonNull(cacheManager.getCacheManager())
                .getCache(cacheName)
                .putIfAbsent(inId, outId);
    }
}
