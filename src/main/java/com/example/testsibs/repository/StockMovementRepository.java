package com.example.testsibs.repository;

import com.example.testsibs.model.entity.StockMovement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StockMovementRepository extends JpaRepository<StockMovement, Long> {

    @Query("SELECT s FROM StockMovement s WHERE s.inStock > 0 AND s.item.id = :itemId ORDER BY s.creationDate")
    List<StockMovement> findByUnprocessedItem(@Param("itemId") Long itemId);

    @Query(value = "SELECT s.* FROM TB_STOCK_MOVEMENT s JOIN TB_ORDER_STOCK o ON s.id = o.stock_movement_id WHERE o.order_id = :orderId", nativeQuery = true)
    List<StockMovement> findByOrderId(@Param("orderId") Long id);
}
