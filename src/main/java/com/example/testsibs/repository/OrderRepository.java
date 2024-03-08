package com.example.testsibs.repository;

import com.example.testsibs.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByItemIdAndProcessedFalseOrderByCreationDate(Long id);

    @Query(value = "SELECT o.* FROM TB_ORDER o JOIN TB_ORDER_STOCK s ON o.id = s.order_id WHERE s.stock_movement_id = :stockMovementId", nativeQuery = true)
    List<Order> findByOrderStocksStockMovementId(@Param("stockMovementId") Long id);

    int countByItemIdAndProcessedFalse(Long id);
}
