package com.pigbox.ddd.domain.repository;

import com.pigbox.ddd.domain.model.entity.TicketDetail;

import java.util.List;
import java.util.Optional;

public interface TicketDetailRepository {
    Optional<TicketDetail> findById(Long id);

    Optional<List<TicketDetail>> findByIds(List<Long> id);

    int updateStockAvailable(Long id, Integer stockAvailable);
}
