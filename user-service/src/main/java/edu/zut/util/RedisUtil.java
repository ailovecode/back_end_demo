package edu.zut.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;


import java.util.Arrays;

/**
 * Redis工具类
 *
 * @version 1.0
 * @Author zhy
 * @Date 2025/6/25 10:14
 */
@Component
public class RedisUtil {

    private static final Logger logger = LoggerFactory.getLogger(RedisUtil.class);

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 在 Spring Boot 中，RedisTemplate 是由 Spring 自动配置的。
     * Spring Boot 的 spring-boot-starter-data-redis 会自动根据 application.yml
     * 或 application.properties 中的 Redis 配置生成
     * RedisTemplate 实例，并将其作为一个 Bean 注入到 Spring 容器中。
     *
     * 从 Spring Framework 4.3 开始，如果类只有一个构造函数，Spring 会自动推断并使用这个构造函数进行依赖注入，无需显式地使用 @Autowired 注解。
     * 由于 RedisUtil 类中只有一个构造函数，因此 Spring 自动会将 RedisTemplate<String, Object> 注入到 RedisUtil 中。
     *
     * @param redisTemplate
     */
    @Autowired
    public RedisUtil(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 设置缓存失效时间
     *
     * @param key 键
     * @param time 时间(秒)
     */
    public boolean expire(String key, long time) {
        if (time > 0) {
            return Boolean.TRUE.equals(redisTemplate.expire(key, time, TimeUnit.SECONDS));
        }
        return false;
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(Arrays.asList(key));
            }
        }
    }

    /**
     * 获取普通缓存
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue()
                .get(key);
    }

    /**
     * 泛型方法 get：<T> 使得 get 方法可以返回不同类型的对象，clazz 参数是对象的类型信息，传入后可以将存储的值反序列化为相应类型。
     * Redis 中的数据必须是通过 GenericJackson2JsonRedisSerializer 或类似的 JSON 序列化器存储的，确保可以成功反序列化。
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(String key, Class<T> clazz) {
        try {
            Object value = redisTemplate.opsForValue()
                    .get(key);
            if (value != null) {
                // 自动转换成指定类型
                return clazz.cast(value);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 普通缓存放入
     *
     * @param key 键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue()
                    .set(key, value);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key 键
     * @param value 值
     * @param time 时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue()
                        .set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return false;
        }
    }
}
