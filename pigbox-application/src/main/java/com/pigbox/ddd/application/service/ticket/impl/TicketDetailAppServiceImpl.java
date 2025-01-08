package com.pigbox.ddd.application.service.ticket.impl;

import com.pigbox.ddd.application.service.ticket.TicketDetailAppService;
import com.pigbox.ddd.application.service.ticket.cache.TicketDetailCacheService;
import com.pigbox.ddd.domain.model.entity.TicketDetail;
import com.pigbox.ddd.domain.model.enums.ResultCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TicketDetailAppServiceImpl implements TicketDetailAppService {

    @Autowired
    private TicketDetailCacheService ticketDetailCacheService;

    @Override
    public TicketDetail getTicketDetailById(Long ticketId) {
        return ticketDetailCacheService.getTicketDetail(ticketId, System.currentTimeMillis());
    }

    @Override
    public ResultCode buyTicket(Long ticketId, Integer quantity) {
        return ticketDetailCacheService.buyTicket(ticketId, quantity);
    }

}
