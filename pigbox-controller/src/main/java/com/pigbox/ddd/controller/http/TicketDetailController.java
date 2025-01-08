package com.pigbox.ddd.controller.http;

import com.pigbox.ddd.application.service.ticket.TicketDetailAppService;
import com.pigbox.ddd.controller.model.enums.ResultUtil;
import com.pigbox.ddd.controller.model.vo.ResultMessage;
import com.pigbox.ddd.controller.model.vo.dto.BuyTicketRequestDTO;
import com.pigbox.ddd.domain.model.entity.TicketDetail;
import com.pigbox.ddd.domain.model.enums.ResultCode;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ticket")
@Slf4j
public class TicketDetailController {

    @Autowired
    private TicketDetailAppService ticketDetailAppService;

    @GetMapping("/{ticketId}/detail/{detailId}")
    public ResultMessage<TicketDetail> getTicketDetail(@PathVariable("ticketId") Long ticketId, @PathVariable("detailId") Long detailId) {
        log.info("ticketId: {}, detailId: {}", ticketId, detailId);
        return ResultUtil.data(ticketDetailAppService.getTicketDetailById(detailId));
    }


    @PostMapping("/buy")
    public ResultMessage<ResultCode> buy(@Valid @RequestBody BuyTicketRequestDTO ticketDetail) {
        log.info("buy >> ticketId: {} with number {}", ticketDetail.getTicketId(), ticketDetail.getQuantity());
        return ResultUtil.data(ticketDetailAppService.buyTicket(ticketDetail.getTicketId(), ticketDetail.getQuantity()));
    }

}
