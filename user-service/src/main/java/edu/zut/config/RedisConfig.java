package edu.zut.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/25 10:14
 */
@Configuration
@EnableCaching
public class RedisConfig {

    // 配置 RedisTemplate，用于操作 Redis 数据库
    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        // 创建 RedisTemplate 对象，用于执行 Redis 操作
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        // 设置连接工厂，用于与 Redis 建立连接
        redisTemplate.setConnectionFactory(factory);

        // 设置 key 的序列化器，使用 StringRedisSerializer 将 key 序列化为字符串
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // 设置 value 的序列化器，使用 GenericJackson2JsonRedisSerializer 将对象序列化为 JSON
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // 设置 hash key 的序列化器，使用 StringRedisSerializer 序列化 hash 的 key
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        // 设置 hash value 的序列化器，使用 Jackson2JsonRedisSerializer 将 hash 值序列化为 JSON
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<Object>(Object.class));

        // 返回配置好的 RedisTemplate
        return redisTemplate;
    }

    // 配置 RedisCacheManager，用于管理 Redis 缓存
    @Bean
    public RedisCacheManager redisCacheManager(RedisTemplate redisTemplate) {
        // 创建 RedisCacheWriter，nonLockingRedisCacheWriter 表示不使用锁机制进行缓存操作
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisTemplate.getConnectionFactory());

        // 配置 RedisCacheConfiguration，指定缓存值的序列化方式，使用 RedisTemplate 中配置的 ValueSerializer 进行序列化
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()));

        // 返回配置好的 RedisCacheManager
        return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);
    }
}
