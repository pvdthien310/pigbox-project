package com.pigbox.ddd.infrastructure.distributed.redisson;

public interface RedisDistributedService {
    RedisDistributedLocker getDistributedLock(String key);
}
