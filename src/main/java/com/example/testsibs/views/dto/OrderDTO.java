package com.example.testsibs.views.dto;

import com.example.testsibs.views.vo.ItemVO;
import com.example.testsibs.views.vo.UserVO;
import lombok.Data;

@Data
public class OrderDTO {

    private ItemVO item;
    private Integer quantity;
    private UserVO user;
}
