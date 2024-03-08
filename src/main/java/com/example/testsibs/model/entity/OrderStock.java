package com.example.testsibs.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "TB_ORDER_STOCK")
@NoArgsConstructor
@Getter
@Setter
public class OrderStock {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "ORDER_ID", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "STOCK_MOVEMENT_ID", nullable = false)
    private StockMovement stockMovement;

    public OrderStock(Order order, StockMovement stockMovement) {
        this.order = order;
        this.stockMovement = stockMovement;
    }

}
