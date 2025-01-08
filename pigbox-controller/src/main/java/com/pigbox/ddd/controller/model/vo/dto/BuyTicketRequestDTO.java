package com.pigbox.ddd.controller.model.vo.dto;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class BuyTicketRequestDTO {
    @NotNull
    private Long ticketId;
    @NotNull
    private Integer quantity;

}
