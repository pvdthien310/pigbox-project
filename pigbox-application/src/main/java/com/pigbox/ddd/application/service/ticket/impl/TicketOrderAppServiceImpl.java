package com.pigbox.ddd.application.service.ticket.impl;

import com.pigbox.ddd.application.service.ticket.TicketOrderAppService;
import com.pigbox.ddd.application.service.ticket.cache.StockOrderCacheService;
import com.pigbox.ddd.domain.messing.ticket.producer.DataReconciliationProducer;
import com.pigbox.ddd.domain.model.entity.InconsistencyRecord;
import com.pigbox.ddd.domain.model.enums.ResultCode;
import com.pigbox.ddd.domain.service.TicketDetailDomainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TicketOrderAppServiceImpl implements TicketOrderAppService {

    @Autowired
    private StockOrderCacheService stockOrderCacheService;
    @Autowired
    private TicketDetailDomainService ticketDetailDomainService;
    @Autowired
    private DataReconciliationProducer<InconsistencyRecord> dataReconciliationProducer;

    @Override
    public ResultCode buyTicket(Long ticketId, Integer quantity) {
        try {
            int isSuccess = stockOrderCacheService.decreaseStockInRedisCacheUsingLUA(ticketId, quantity);
            if (isSuccess == 0) {
                log.info("Case: oldStockAvailable < quantity={}, ticketId {}", quantity, ticketId);
                return ResultCode.PRODUCT_SKU_QUANTITY_NOT_ENOUGH;
            }
            int isDbSuccess = ticketDetailDomainService.updateStockAvailable(ticketId, quantity, MAX_RETRY_TIMES);
            if (isDbSuccess == 0) {
                log.warn("Case: update stock for ticketId {} failed with quantity: {}", ticketId, quantity);
                stockOrderCacheService.increaseStockInRedisCacheUsingLUA(ticketId, quantity);
                dataReconciliationProducer.enqueueInconsistency(
                        List.of(InconsistencyRecord.builder()
                                .ticketId(ticketId)
                                .quantity(quantity)
                                .build())
                );
            }
            return ResultCode.SUCCESS;
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            dataReconciliationProducer.enqueueInconsistency(
                    List.of(InconsistencyRecord.builder()
                            .ticketId(ticketId)
                            .quantity(quantity)
                            .build())
            );
        }
        return ResultCode.SUCCESS;
    }

    private static final Integer MAX_RETRY_TIMES = 3;
}
