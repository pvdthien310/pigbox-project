package com.pigbox.ddd.application.service.ticket;

import com.pigbox.ddd.domain.model.enums.ResultCode;

public interface TicketOrderAppService {
    ResultCode buyTicket(Long ticketId, Integer quantity);
}
