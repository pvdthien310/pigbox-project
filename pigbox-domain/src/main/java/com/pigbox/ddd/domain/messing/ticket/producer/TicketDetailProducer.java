package com.pigbox.ddd.domain.messing.ticket.producer;

import java.util.List;

public interface TicketDetailProducer<T> {
    void publish(List<T> messages);
}
