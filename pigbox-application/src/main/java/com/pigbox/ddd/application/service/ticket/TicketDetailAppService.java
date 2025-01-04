package com.pigbox.ddd.application.service.ticket;

import com.pigbox.ddd.domain.model.entity.TicketDetail;

public interface TicketDetailAppService {
    TicketDetail getTicketDetailById(Long ticketId);
}
