package com.pigbox.ddd.domain.service.impl;

import com.pigbox.ddd.domain.model.entity.TicketDetail;
import com.pigbox.ddd.domain.repository.TicketDetailRepository;
import com.pigbox.ddd.domain.service.TicketDetailDomainService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class TicketDetailDomainServiceImpl implements TicketDetailDomainService {

    @Autowired
    private TicketDetailRepository ticketDetailRepository;

    @Override
    public TicketDetail getTicketDetailById(Long ticketId) {
        log.info("Implement Domain : {}", ticketId);
        return ticketDetailRepository.findById(ticketId).orElse(null);
    }

    @Override
    @Transactional
    public int updateStockAvailable(Long ticketId, Integer quantity, Integer maxRetryTimes) {
        for (int i = 0; i < maxRetryTimes; i++) {
            int rowAffected = ticketDetailRepository.updateStockAvailable(ticketId, quantity);
            if (rowAffected > 0) {
                return rowAffected;
            }
        }
        return 0;
    }

}
