package com.example.testsibs.views.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderVO {

    private Long id;
    private LocalDateTime creationDate;
    private ItemVO item;
    private Integer quantity;
    private boolean processed;
    private UserVO user;
}
