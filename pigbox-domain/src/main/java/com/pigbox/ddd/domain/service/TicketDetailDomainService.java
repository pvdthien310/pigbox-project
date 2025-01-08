package com.pigbox.ddd.domain.service;

import com.pigbox.ddd.domain.model.entity.TicketDetail;

public interface TicketDetailDomainService {

    TicketDetail getTicketDetailById(Long id);

    void updateStockAvailable(Long id, Integer stockAvailable);
}
