package edu.zut.util;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.IdUtil;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 雪花算法生成分布式 ID
 * AI
 */
public class SnowflakeUtils {
    private Snowflake snowflake;

    private static class IdGenHolder {
        private static final SnowflakeUtils instance = new SnowflakeUtils();
    }

    public static SnowflakeUtils get() {
        return IdGenHolder.instance;
    }

    private SnowflakeUtils() {
        String ipv4 = NetUtil.getLocalhostStr();
        long ipLong = Long.parseLong(ipv4.replaceAll("\\.", ""));
        long workerId = Long.hashCode(ipLong) % 32;
        long datacenterId = 2L;
        snowflake = IdUtil.createSnowflake(workerId, datacenterId);
    }

    /**
     * 修改点：在序列号初始化为随机值（1-3），避免总是4的倍数
     */
    public synchronized long id() {
        long rawId = snowflake.nextId();
        // 获取序列号部分（最后12位）
        long sequence = rawId & 0xFFF; // 0xFFF对应12位序列号
        // 若序列号为0（即ID是4的倍数），添加随机偏移量1~3
        if (sequence == 0) {
            return rawId + ThreadLocalRandom.current().nextInt(1, 4);
        }
        return rawId;
    }
}