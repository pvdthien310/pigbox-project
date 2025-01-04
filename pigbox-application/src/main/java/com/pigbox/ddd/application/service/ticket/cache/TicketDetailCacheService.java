package com.pigbox.ddd.application.service.ticket.cache;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.pigbox.ddd.domain.model.entity.TicketDetail;
import com.pigbox.ddd.domain.service.TicketDetailDomainService;
import com.pigbox.ddd.infrastructure.cache.redis.RedisInfrasService;
import com.pigbox.ddd.infrastructure.distributed.redisson.RedisDistributedLocker;
import com.pigbox.ddd.infrastructure.distributed.redisson.RedisDistributedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Slf4j
@Service
public class TicketDetailCacheService {

    @Autowired
    private RedisInfrasService redisInfrasService;
    @Autowired
    private RedisDistributedService redisDistributedService;
    @Autowired
    private TicketDetailDomainService ticketDetailDomainService;

    // Guava cache
    private final static Cache<Long, TicketDetail> ticketDetailLocalCache =
            CacheBuilder.newBuilder()
                .initialCapacity(10)
                .concurrencyLevel(12)
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .build();

    private TicketDetail getTicketDetailFromLocalCache(Long ticketId) {
        try {
            return ticketDetailLocalCache.getIfPresent(ticketId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public TicketDetail getTicketDetail(Long ticketId, Long version) {
        // 1. Get Local Cache
        TicketDetail ticketDetail = getTicketDetailFromLocalCache(ticketId);

        if (Objects.nonNull(ticketDetail)) {
            log.info("FROM LOCAL CACHE EXIST {}", ticketDetail);
            return ticketDetail;
        }

        // 2. Get from Redis
        ticketDetail = getDataFromCache(ticketId);

        if (Objects.nonNull(ticketDetail)) {
            log.info("FROM REDIS CACHE EXIST {}", ticketDetail);
            ticketDetailLocalCache.put(ticketId, ticketDetail);
            return ticketDetail;
        }

        // 3. Try lock and get data from database
        RedisDistributedLocker locker = redisDistributedService.getDistributedLock("PRO_LOCK_KEY_ITEM" + ticketId);

        try {
            // Try lock
            boolean isLock = locker.tryLock(1, 5, TimeUnit.SECONDS);
            // Luu y: Cho du thanh cong hay khong thanh cong thi ta van  unlock

            if (!isLock) {
                return ticketDetail;
            }

            // get Cache la nua
            ticketDetail = getDataFromCache(ticketId);

            if (Objects.nonNull(ticketDetail)) {
                log.info("FROM CACHE SAU KHI DC LOCK {} {} {}", ticketId, version, ticketDetail);
                ticketDetailLocalCache.put(ticketId, ticketDetail);
                return ticketDetail;
            }

            // truy van db
            ticketDetail = ticketDetailDomainService.getTicketDetailById(ticketId);
            log.info("FROM DBS ->>>> {}, {}", ticketId, version);
            if (Objects.isNull(ticketDetail)) {
                log.info("TICKET NOT EXISTS.....{}, {}", ticketId, version);
                redisInfrasService.setObject(getEventItemKey(ticketId), ticketDetail);
                ticketDetailLocalCache.put(ticketId, null);
                return ticketDetail;
            }

            redisInfrasService.setObject(getEventItemKey(ticketId), ticketDetail);
            ticketDetailLocalCache.put(ticketId, ticketDetail);
            return ticketDetail;

        } catch (InterruptedException e) {
            ticketDetail = getDataFromCache(ticketId);

            if (Objects.nonNull(ticketDetail)) {
                log.info("FROM CACHE AFTER TIME OUT TRY LOCK {}", ticketDetail);
                ticketDetailLocalCache.put(ticketId, ticketDetail);
                return ticketDetail;
            }
            log.info("getTicketDefaultCacheVip got timeout try lock exception:", e);
        } catch (Exception e) {
            log.info("getTicketDefaultCacheVip got exception:", e);
        } finally {
            locker.unlock();
        }
        return null;
    }

    private TicketDetail getDataFromCache(Long ticketId) {
        return redisInfrasService.getOject(getEventItemKey(ticketId), TicketDetail.class);
    }

    private String getEventItemKey(Long id) {
        return id.toString();
    }

}
