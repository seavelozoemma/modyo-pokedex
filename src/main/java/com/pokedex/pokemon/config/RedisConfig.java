package com.pokedex.pokemon.config;

import com.pokedex.pokemon.config.properties.GlobalProperties;
import com.pokedex.pokemon.config.properties.RedisProperties;
import com.pokedex.pokemon.config.properties.SpringProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig {

    private static final String POKE_CACHE = "poke-cache";
    private static final String KEY_DELIMITER= "::";

    private final RedisProperties redisProperties;
    private final SpringProperties springProperties;
    private final GlobalProperties globalProperties;

    public RedisConfig(RedisProperties redisProperties, SpringProperties springProperties, GlobalProperties globalProperties) {
        this.redisProperties = redisProperties;
        this.springProperties = springProperties;
        this.globalProperties = globalProperties;
    }

    @Bean
    LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConf = new RedisStandaloneConfiguration();
        redisConf.setHostName(springProperties.getRedis().getHost());
        redisConf.setPort(Integer.parseInt(springProperties.getRedis().getPort()));
        redisConf.setPassword(RedisPassword.of(springProperties.getRedis().getSecret()));
        return new LettuceConnectionFactory(redisConf);
    }

    @Bean
    @Autowired
    CacheManager cacheManager(LettuceConnectionFactory redisConnectionFactory) {
        return RedisCacheManager
                .RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(getTtlConfiguration(Duration.ofHours(redisProperties.getDuration().getDefaultValue())))
                .withCacheConfiguration(POKE_CACHE, getTtlConfiguration(Duration.ofMinutes(redisProperties.getDuration().getDefaultValue())))
                .build();

    }

    @Bean
    RedisCacheWriter redisCacheWriter() {
        return RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory());
    }

    @Bean
    RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration
                .defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();
    }


    private RedisCacheConfiguration getTtlConfiguration(Duration duration) {
        return this
                .cacheConfiguration()
                .computePrefixWith(
                        cacheName -> globalProperties.getProfile().concat(KEY_DELIMITER).concat(cacheName).concat(KEY_DELIMITER)
                ).entryTtl(duration);
    }

}
