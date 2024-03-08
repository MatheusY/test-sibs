package com.example.testsibs.service;

import com.example.testsibs.model.entity.Order;
import com.example.testsibs.model.entity.OrderStock;
import com.example.testsibs.model.entity.StockMovement;
import com.example.testsibs.repository.OrderRepository;
import com.example.testsibs.repository.OrderStockRepository;
import com.example.testsibs.repository.StockMovementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class OrderStockServiceImpl extends LoggingService<OrderStock>  implements IOrderStockService {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderStockRepository orderStockRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void checkIsOrderPendent(StockMovement stockMovement) {
        List<Order> orders = orderRepository.findByItemIdAndProcessedFalseOrderByCreationDate(stockMovement.getItem().getId());
        if(!orders.isEmpty()) {
            updateData(orders, stockMovementRepository.findByUnprocessedItem(stockMovement.getItem().getId()));
        }
    }

    public void checkHasStock(Order order) {
        int count = orderRepository.countByItemIdAndProcessedFalse(order.getItem().getId());
        if(count == 1) {
            updateData(Arrays.asList(order), stockMovementRepository.findByUnprocessedItem(order.getItem().getId()));
        }
    }

    private void updateData(List<Order> orders, List<StockMovement> stockMovements) {
        orders.forEach(entityManager::detach);
        stockMovements.forEach(entityManager::detach);
        List<OrderStock> orderStocks = new ArrayList<>();
        int index = 0;
        for(Order order : orders) {
            List<StockMovement> stockMovs = new ArrayList<>();
            int qtd = 0;
            while(index < stockMovements.size() && qtd < order.getQuantity()) {
                StockMovement inStock = stockMovements.get(index);
                qtd += inStock.getInStock();
                inStock.setInStock(0);
                stockMovs.add(inStock);
                index++;
            }
            if(index == stockMovements.size() && qtd < order.getQuantity()) {
                break;
            }
            if(qtd > order.getQuantity()) {
                index--;
                stockMovements.get(index).setInStock(qtd - order.getQuantity());
            }
            order.setProcessed(true);
            stockMovements.forEach(inStock -> orderStocks.add(new OrderStock(order, inStock)));
        }
        Set<Order> processedOrders = orderStocks.stream().map(OrderStock::getOrder).collect(Collectors.toSet());
        orderRepository.saveAll(processedOrders);
        Set<StockMovement> stockMovementsUsed = orderStocks.stream().map(OrderStock::getStockMovement).collect(Collectors.toSet());
        stockMovementRepository.saveAll(stockMovementsUsed);
        orderStockRepository.saveAll(orderStocks);
        logger.info(String.format("Orders completed [%s]", processedOrders.stream().map(order -> order.getId().toString()).collect(Collectors.joining(", "))));
        logger.info(String.format("Stock Movements completed [%s]", stockMovementsUsed.stream().map(stockMovement -> stockMovement.getId().toString()).collect(Collectors.joining(", "))));
        processedOrders.forEach(this::notifyUser);
    }

    private void notifyUser(Order order){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(order.getUser().getEmail());
        message.setSubject("Order completed");
        message.setText(String.format("The order for %s with %d units requested on %s is completed.", order.getItem().getName(), order.getQuantity(), order.getCreationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));

        mailSender.send(message);
        logger.info("Email sent to order id " + order.getId());
    }
}
