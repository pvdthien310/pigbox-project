package com.pigbox.ddd.domain.messing.ticket.listener;

import org.springframework.kafka.support.Acknowledgment;

import java.util.List;

public interface DataReconciliationListener<T> {
    void receiveInconsistencySync(List<T> messages, List<String> keys,
                                  List<Integer> partitions, List<Long> offsets,
                                  Acknowledgment ack);
}
