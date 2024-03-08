package com.example.testsibs.service;

import com.example.testsibs.model.entity.Order;
import com.example.testsibs.model.entity.StockMovement;

import java.util.List;

public interface IOrderStockService {

    void checkIsOrderPendent(StockMovement stockMovement);

    void checkHasStock(Order order);
}
