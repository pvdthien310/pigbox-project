package com.pigbox.ddd.domain.model.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class InconsistencyRecord {
    private long ticketId;
    private int quantity;
}
