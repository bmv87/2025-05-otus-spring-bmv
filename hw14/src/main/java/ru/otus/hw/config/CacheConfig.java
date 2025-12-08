package ru.otus.hw.config;

import org.ehcache.config.ResourcePools;
import org.ehcache.config.builders.CacheConfigurationBuilder;
import org.ehcache.config.builders.ExpiryPolicyBuilder;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import org.ehcache.config.units.EntryUnit;
import org.ehcache.config.units.MemoryUnit;
import org.ehcache.jsr107.Eh107Configuration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.hw.cache.CacheHelper;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.spi.CachingProvider;
import java.time.Duration;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager ehCacheManager() {
        CachingProvider provider = Caching.getCachingProvider();
        CacheManager cacheManager = provider.getCacheManager();
        ResourcePools resourcePools = ResourcePoolsBuilder.newResourcePoolsBuilder()
                .heap(2000, EntryUnit.ENTRIES)
                .offheap(100, MemoryUnit.MB)
                .build();
        var expiryPolicy = ExpiryPolicyBuilder.timeToLiveExpiration(Duration.ofDays(1));
        CacheConfigurationBuilder<Long, Long> configuration =
                CacheConfigurationBuilder.newCacheConfigurationBuilder(
                                Long.class,
                                Long.class,
                                resourcePools)
                        .withExpiry(expiryPolicy);

        javax.cache.configuration.Configuration<Long, Long> cacheConfiguration =
                Eh107Configuration.fromEhcacheCacheConfiguration(configuration);

        cacheManager.createCache(CacheHelper.AUTHORS, cacheConfiguration);
        cacheManager.createCache(CacheHelper.GENRES, cacheConfiguration);
        cacheManager.createCache(CacheHelper.BOOKS, cacheConfiguration);
        return cacheManager;

    }
}
