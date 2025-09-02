package com.pigbox.ddd.infrastructure.persistence.repository;

import com.pigbox.ddd.domain.model.entity.TicketDetail;
import com.pigbox.ddd.domain.repository.TicketDetailRepository;
import com.pigbox.ddd.infrastructure.persistence.mapper.mysql.TicketDetailJPAMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
    public Optional<List<TicketDetail>> findByIds(List<Long> ids) {
        return ticketDetailJPAMapper.findAllByIdIsIn(ids);
    }

    @Override
    @Transactional
    public int updateStockAvailable(Long id, Integer quantity) {
        return ticketDetailJPAMapper.updateStockAvailableUsingOptimisticLock(id, quantity);
    }
}
