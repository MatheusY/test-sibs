package com.example.testsibs.model.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="TB_ORDER")
@Getter
@Setter
public class Order {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "CREATION_DATE", nullable = false)
    private LocalDateTime creationDate;

    @ManyToOne
    @JoinColumn(name = "ITEM_ID", nullable = false)
    private Item item;

    @Column(name = "QUANTITY", nullable = false)
    private Integer quantity;

    @Column(name = "PROCESSED")
    private boolean processed;

    @ManyToOne
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;
}
