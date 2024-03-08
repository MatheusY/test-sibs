package com.example.testsibs.service;

import com.example.testsibs.model.entity.StockMovement;

import java.util.List;

public interface IStockMovementService extends AbstractService<StockMovement> {

    List<StockMovement> findByOrder(Long id);
}
