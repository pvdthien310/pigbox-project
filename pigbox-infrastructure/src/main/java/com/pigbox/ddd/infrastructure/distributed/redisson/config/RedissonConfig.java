package com.pigbox.ddd.infrastructure.distributed.redisson.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

//    @Bean
//    public RedissonClient redissonClient() {
//        Config config = new Config();
//        config.useSingleServer().setAddress("redis://127.0.0.1:6319").setDatabase(0);
//        return Redisson.create(config);
//    }


    @Bean
    public RedissonClient redisson() {
        Config config = new Config();
        config.useSentinelServers()
                .setMasterName("mymaster")
                .addSentinelAddress(
                        "redis://192.168.0.107:26379",
                        "redis://192.168.0.107:26380",
                        "redis://192.168.0.107:26381"
                )
                .setCheckSentinelsList(false)
                .setDatabase(0)
                .setMasterConnectionPoolSize(50)
                .setMasterConnectionMinimumIdleSize(10)
                .setSlaveConnectionPoolSize(50)
                .setSlaveConnectionMinimumIdleSize(10);

        return Redisson.create(config);
    }
}
