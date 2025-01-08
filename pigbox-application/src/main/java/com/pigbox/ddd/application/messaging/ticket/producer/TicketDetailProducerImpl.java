package com.pigbox.ddd.application.messaging.ticket.producer;

import com.pigbox.ddd.domain.messing.ticket.producer.TicketDetailProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketDetailProducerImpl implements TicketDetailProducer<Long> {

    @Autowired
    private final KafkaTemplate<String, Object> template;

    @Override
    public void publish(List<Long> ticketIds) {
        ticketIds.forEach(ticketId -> {
            int partitionId = 0;
            String key = UUID.randomUUID().toString();
            ProducerRecord<String, Object> message =
                    new ProducerRecord<>("reset-topic", partitionId, key, ticketId, createHeaders(partitionId, key));
            template.send(message);
            log.info("TicketDetailProducerImpl pulished message with ticketId {}", ticketId);
        });
    }

    private Iterable<Header> createHeaders(int partitionId, String key) {
        List <Header> headers = new ArrayList<>();
        headers.add(new RecordHeader("kafka_partitionId", String.valueOf(partitionId).getBytes()));
        headers.add(new RecordHeader("kafka_messageKey", key.getBytes()));
        headers.add(new RecordHeader("kafka_offset", String.valueOf(0).getBytes()));
        return headers;
    }
}
