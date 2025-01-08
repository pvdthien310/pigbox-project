package com.pigbox.ddd.infrastructure.persistence.repository;

import com.pigbox.ddd.domain.model.entity.TicketDetail;
import com.pigbox.ddd.domain.repository.TicketDetailRepository;
import com.pigbox.ddd.infrastructure.persistence.mapper.mysql.TicketDetailJPAMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
public class TicketDetailInfrasRepositoryImpl implements TicketDetailRepository  {

    @Autowired
    private TicketDetailJPAMapper ticketDetailJPAMapper;

    @Override
    public Optional<TicketDetail> findById(Long id) {
        return ticketDetailJPAMapper.findById(id);
    }

    @Override
    @Transactional
    public void updateStockAvailable(Long id, Integer stockAvailable) {
        ticketDetailJPAMapper.updateStockAvailable(id, stockAvailable);
    }
}
