package com.pigbox.ddd.domain.service.impl;

import com.pigbox.ddd.domain.model.entity.TicketDetail;
import com.pigbox.ddd.domain.repository.TicketDetailRepository;
import com.pigbox.ddd.domain.service.TicketDetailDomainService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

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
    public void updateStockAvailable(Long ticketId, Integer stockAvailable) {
        TicketDetail ticketDetail = getTicketDetailById(ticketId);

        if (Objects.isNull(ticketDetail)) {
            throw new RuntimeException("Product is not available");
        }

        if (ticketDetail.getStockAvailable() < stockAvailable) {

            throw new RuntimeException("Quantity is not enough");
        }

        ticketDetailRepository.updateStockAvailable(ticketId, stockAvailable);

    }

}
