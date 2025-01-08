package com.pigbox.ddd.domain.messing.ticket.listener;

import java.util.List;

public interface TicketDetailListener<T> {
    void receive(List<T> messages, List<String> keys, List<Integer> partitions, List<Long> offsets);
}

