package com.pigbox.ddd.application.service.ticket;

import com.pigbox.ddd.domain.model.entity.TicketDetail;
import com.pigbox.ddd.domain.model.enums.ResultCode;

public interface TicketDetailAppService {
    TicketDetail getTicketDetailById(Long ticketId);
    ResultCode buyTicket(Long ticketId, Integer quantity);
}
