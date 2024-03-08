package com.example.testsibs.repository;

import com.example.testsibs.model.entity.OrderStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStockRepository extends JpaRepository<OrderStock, Long> {
}
