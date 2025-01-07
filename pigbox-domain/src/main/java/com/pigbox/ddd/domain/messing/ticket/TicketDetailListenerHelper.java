package com.pigbox.ddd.domain.messing.ticket;

import java.util.List;

public interface TicketDetailListenerHelper<T> {
    void receive(List<T> messages, List<String> keys, List<Integer> partitions, List<Long> offsets);
}
