package com.example.testsibs.service;

import com.example.testsibs.exception.InvalidFieldsException;
import com.example.testsibs.model.entity.StockMovement;
import com.example.testsibs.repository.StockMovementRepository;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StockMovementServiceImpl extends LoggingService<StockMovement> implements IStockMovementService {

    @Autowired
    private StockMovementRepository stockMovementRepository;

    @Autowired
    private IOrderStockService orderStockService;

    @Override
    public List<StockMovement> list() {
        return stockMovementRepository.findAll();
    }

    @Override
    public StockMovement findById(Long id) throws NotFoundException {
        try {
            return stockMovementRepository.findById(id).orElseThrow(() -> new NotFoundException("Stock Movement not found!"));
        } catch (NotFoundException ex) {
            logger.error(String.format("[ERROR] - %s", id), ex);
            throw new NotFoundException(ex.getMessage());
        } catch (Exception ex) {
            logger.error(String.format("[ERROR] - %s", id), ex);
            throw ex;
        }
    }

    @SneakyThrows
    @Override
    public StockMovement create(StockMovement stockMovement) throws InvalidFieldsException {
        return loggingCreate(() -> {
            checkData(stockMovement);
            stockMovement.setId(null);
            stockMovement.setInStock(stockMovement.getQuantity());
            stockMovement.setCreationDate(LocalDateTime.now());
            stockMovementRepository.save(stockMovement);
            orderStockService.checkIsOrderPendent(stockMovement);
            return stockMovement;
        }, stockMovement);
    }

    @SneakyThrows
    @Override
    public void update(StockMovement stockMovement) throws NotFoundException, InvalidFieldsException {
        checkIfExists(stockMovement.getId());
        loggingCreate(() ->
                        stockMovementRepository.save(stockMovement)
                , stockMovement);
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        checkIfExists(id);
        stockMovementRepository.deleteById(id);
    }

    @Override
    public List<StockMovement> findByOrder(Long id) {
        return stockMovementRepository.findByOrderId(id);
    }

    private static void checkData(StockMovement stockMovement) {
        if (stockMovement.getQuantity() <= 0) {
            throw new IllegalArgumentException("Invalid quantity!");
        }
    }

    private void checkIfExists(Long id) throws NotFoundException {
        if (!stockMovementRepository.existsById(id)) {
            logger.error("[ERROR] - Stock Movement Not Found Exception " + id);
            throw new NotFoundException("Stock Movement not found!");
        }
    }
}
