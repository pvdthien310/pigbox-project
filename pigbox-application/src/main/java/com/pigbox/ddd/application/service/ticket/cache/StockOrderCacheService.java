package com.pigbox.ddd.application.service.ticket.cache;

import com.pigbox.ddd.infrastructure.cache.redis.RedisInfrasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
public class StockOrderCacheService {
    @Autowired
    private RedisInfrasService redisInfrasService;

    private String getEventItemKey(Long id) {
        return id.toString();
    }

    public int decreaseStockInRedisCacheUsingLUA(Long ticketId, Integer quantity) {
        String luaScript = """
            local stock = tonumber(redis.call('get', KEYS[1]));
            if (stock >= tonumber(ARGV[1])) then
                redis.call('SET', KEYS[1], stock - tonumber(ARGV[1]));
                return 1;
            end;
            return 0;
        """;

        log.info("ticket: {} selling...: remaining number: {} ", ticketId, quantity);
        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
        Long result = redisInfrasService.getRedisTemplate().execute(redisScript, Collections.singletonList("STOCK:" + getEventItemKey(ticketId)), quantity);
        return result.intValue();
    }

    public int increaseStockInRedisCacheUsingLUA(Long ticketId, Integer quantity) {
        String luaScript = """
            local stock = tonumber(redis.call('get', KEYS[1]));
            if (stock >= tonumber(ARGV[1])) then
                redis.call('SET', KEYS[1], stock + tonumber(ARGV[1]));
                return 1;
            end;
            return 0;
        """;

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>(luaScript, Long.class);
        Long result = redisInfrasService.getRedisTemplate().execute(redisScript, Collections.singletonList("STOCK:" + getEventItemKey(ticketId)), quantity);
        log.info("ticket: {} LUA update {} ", ticketId, result.intValue());
        return result.intValue();
    }
}
