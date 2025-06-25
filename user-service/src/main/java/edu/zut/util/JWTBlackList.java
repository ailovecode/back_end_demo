package edu.zut.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import jakarta.annotation.Resource;
import org.apache.commons.codec.digest.DigestUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class JWTBlackList {

    @Resource
    RedisUtil redisUtil;

    private static final String PREFIX = "user:jwt:blacklist:";

    /** 添加 token 到黑名单，过期时间取 JWT 自身过期时间 */
    public void add(String token) {
        DecodedJWT jwt = JWT.decode(token);
        long ttlMs = jwt.getExpiresAt().getTime() - System.currentTimeMillis();
        if (ttlMs > 0) {
            // 1. 构造一个长度可控又唯一的 Redis Key
            String key = PREFIX + DigestUtils.md5Hex(token);
            // 2. 选一个不会变的占位符，比如 Boolean.TRUE，或者自己喜欢的字符串
            Object value = Boolean.TRUE;
            // 3. RedisUtil 里的 set 方法接收的过期时间是“秒”单位
            long ttlSeconds = (ttlMs + 999) / 1000;
            // 写入并设置过期
            redisUtil.set(key, value, ttlSeconds);
        }
    }

    /** 判断 token 是否已被加入黑名单 */
    public boolean contains(String token) {
        return redisUtil.hasKey(PREFIX + DigestUtils.md5Hex(token));
    }
}