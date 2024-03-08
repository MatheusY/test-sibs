package com.example.testsibs.service;

import com.example.testsibs.exception.InvalidFieldsException;
import com.example.testsibs.model.entity.Order;
import com.example.testsibs.repository.OrderRepository;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl extends LoggingService<Order> implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private IOrderStockService orderStockService;

    @Autowired
    private IUserService userService;

    @Override
    public List<Order> list() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(Long id) throws NotFoundException {
        try {
            return orderRepository.findById(id).orElseThrow(() -> new NotFoundException("Order not found!"));
        } catch (NotFoundException ex) {
            logger.error(String.format("[ERROR] - %s", id), ex);
            throw new NotFoundException(ex.getMessage());
        } catch (Exception ex) {
            logger.error(String.format("[ERROR] - %s", id), ex);
            throw ex;
        }
    }

    @Override
    public Order create(Order order) throws InvalidFieldsException {
        return loggingCreate(() -> {
            checkData(order);
            order.setId(null);
            order.setProcessed(false);
            order.setCreationDate(LocalDateTime.now());
            orderRepository.save(order);
            order.setUser(userService.getUser(order.getUser().getId()));
            orderStockService.checkHasStock(order);
            return order;
        }, order);

    }

    @Override
    public void update(Order order) throws NotFoundException, InvalidFieldsException {
        checkIsProcessed(order.getId());
        loggingCreate(() -> {
            checkData(order);
            return orderRepository.save(order);
        }, order);
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        checkIsProcessed(id);
        orderRepository.deleteById(id);
    }

    @Override
    public List<Order> findByStockMovement(Long id) {
        return orderRepository.findByOrderStocksStockMovementId(id);
    }

    private static void checkData(Order order) {
        if (order.getQuantity() <= 0) {
            throw new IllegalArgumentException("Invalid quantity!");
        }
    }

    private void checkIsProcessed(Long id) throws NotFoundException {
        Order order = findById(id);
        if (order.isProcessed()) {
            logger.error(String.format("[ERROR] - %d order is already processed"), id);
            throw new NotFoundException("The operation can't be completed, the order is already processed");
        }
    }
}
