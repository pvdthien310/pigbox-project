package com.pigbox.ddd.application.messaging.ticket.producer;

import com.pigbox.ddd.domain.messing.ticket.producer.DataReconciliationProducer;
import com.pigbox.ddd.domain.model.entity.InconsistencyRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataReconciliationProducerImpl implements DataReconciliationProducer<InconsistencyRecord> {

    @Autowired
    private final KafkaTemplate<String, Object> template;

    @Override
    public void enqueueInconsistency(List<InconsistencyRecord> messages) {
        messages.forEach(ticketId -> {
            String key = UUID.randomUUID().toString();
            ProducerRecord<String, Object> message = new ProducerRecord<>("inconsistency-queue", key, ticketId);
            template.send(message);
            log.info("TicketDetailProducerImpl published message with ticketId {}", ticketId);
        });
    }
}
