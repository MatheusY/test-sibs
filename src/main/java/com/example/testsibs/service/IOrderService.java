package com.example.testsibs.service;

import com.example.testsibs.model.entity.Order;

import java.util.List;

public interface IOrderService extends AbstractService<Order>{

    List<Order> findByStockMovement(Long id);
}
