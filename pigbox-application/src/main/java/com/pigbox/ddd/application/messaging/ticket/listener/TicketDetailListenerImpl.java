package com.pigbox.ddd.application.messaging.ticket.listener;

import com.pigbox.ddd.application.service.ticket.cache.TicketDetailCacheService;
import com.pigbox.ddd.domain.messing.ticket.listener.TicketDetailListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class TicketDetailListenerImpl implements TicketDetailListener<Long> {

    @Autowired
    TicketDetailCacheService ticketDetailCacheService;

    @Override
    @KafkaListener(id = "${kafka-consumer-config.ticket-detail-reset-topic-id}", topics = "${ticket-detail-service.reset-topic-name}")
    public void receive(@Payload List<Long> messages,
                        @Header(KafkaHeaders.KEY) List<String> keys,
                        @Header(KafkaHeaders.PARTITION) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("TicketDetailListener >> {} number of messages received with keys:{}, partitions:{} and offsets: {}",
                messages.size(),
                keys.toString(),
                partitions.toString(),
                offsets.toString());

        messages.forEach(value -> ticketDetailCacheService.resetLocalCache(value));
    }
}
