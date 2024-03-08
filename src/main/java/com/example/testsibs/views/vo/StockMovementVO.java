package com.example.testsibs.views.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StockMovementVO {

    private Long id;
    private LocalDateTime creationDate;
    private ItemVO item;
    private Integer quantity;
}
