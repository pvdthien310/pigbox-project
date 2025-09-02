package com.pigbox.ddd.domain.service;

import com.pigbox.ddd.domain.model.entity.TicketDetail;

import java.rmi.UnexpectedException;

public interface TicketDetailDomainService {

    TicketDetail getTicketDetailById(Long id);

    int updateStockAvailable(Long id, Integer stockAvailable, Integer maxRetryTimes) throws UnexpectedException;
}
