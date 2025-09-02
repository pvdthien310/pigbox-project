package com.pigbox.ddd.domain.messing.ticket.producer;

import java.util.List;

public interface DataReconciliationProducer<T> {
    void enqueueInconsistency(List<T> messages);
}
