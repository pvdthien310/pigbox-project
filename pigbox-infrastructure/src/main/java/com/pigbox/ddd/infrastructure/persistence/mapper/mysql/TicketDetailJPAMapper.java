package com.pigbox.ddd.infrastructure.persistence.mapper.mysql;

import com.pigbox.ddd.domain.model.entity.TicketDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TicketDetailJPAMapper extends JpaRepository<TicketDetail, Integer> {
    Optional<TicketDetail> findById(Long id);

    @Modifying
    @Query("UPDATE TicketDetail p SET p.stockAvailable = p.stockAvailable - :stockAvailable WHERE p.id = :id")
    void updateStockAvailable(@Param("id") Long id,  @Param("stockAvailable") Integer stockAvailable);
}
