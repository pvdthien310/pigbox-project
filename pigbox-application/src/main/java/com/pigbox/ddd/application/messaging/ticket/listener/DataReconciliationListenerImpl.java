package com.pigbox.ddd.application.messaging.ticket.listener;

import com.pigbox.ddd.application.service.ticket.cache.StockOrderCacheService;
import com.pigbox.ddd.application.service.ticket.cache.TicketDetailCacheService;
import com.pigbox.ddd.domain.messing.ticket.listener.DataReconciliationListener;
import com.pigbox.ddd.domain.model.entity.InconsistencyRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class DataReconciliationListenerImpl implements DataReconciliationListener<InconsistencyRecord> {

    @Autowired
    TicketDetailCacheService ticketDetailCacheService;

    @Autowired
    StockOrderCacheService stockOrderCacheService;

    @Override
    @KafkaListener(topics = "inconsistency-queue", groupId = "inconsistency-queue-listener-group", containerFactory = "kafkaManualAckListenerContainerFactory")
    public void receiveInconsistencySync(List<InconsistencyRecord> messages, List<String> keys,
                                         List<Integer> partitions, List<Long> offsets,
                                         Acknowledgment ack) {
        for (InconsistencyRecord message : messages) {
            try {
                log.info("receive inconsistency sync message: {}", message);
                int stockInRedis = ticketDetailCacheService.getStockInCache(message.getTicketId());
                int stockInDb = ticketDetailCacheService.getStockInDb(message.getTicketId());

                int diff = stockInDb - stockInRedis;
                if (diff > 0) {
                    stockOrderCacheService.increaseStockInRedisCacheUsingLUA(message.getTicketId(), message.getQuantity());
                } else {
                    stockOrderCacheService.decreaseStockInRedisCacheUsingLUA(message.getTicketId(), message.getQuantity());
                }
                ack.acknowledge();
                log.info("receive inconsistency sync message: {} done", message);
            } catch (Exception e) {
                log.error("receive inconsistency sync message: {} got exception:", message, e);
                ack.acknowledge();
            }
        }
    }
}
