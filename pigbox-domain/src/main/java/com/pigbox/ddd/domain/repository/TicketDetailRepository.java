package com.pigbox.ddd.domain.repository;

import com.pigbox.ddd.domain.model.entity.TicketDetail;

import java.util.Optional;

public interface TicketDetailRepository {
    Optional<TicketDetail> findById(Long id);

    void updateStockAvailable(Long id, Integer stockAvailable);
}
