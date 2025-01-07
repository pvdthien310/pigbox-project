package com.pigbox.ddd.infrastructure.persistence.mapper.mysql;

import com.pigbox.ddd.domain.model.entity.TicketDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TicketDetailJPAMapper extends JpaRepository<TicketDetail, Integer> {
    Optional<TicketDetail> findById(Long id);
}
