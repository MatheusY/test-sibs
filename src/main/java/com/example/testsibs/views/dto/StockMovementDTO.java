package com.example.testsibs.views.dto;

import com.example.testsibs.views.vo.ItemVO;
import lombok.Data;

@Data
public class StockMovementDTO {

    private ItemVO item;
    private Integer quantity;
}
