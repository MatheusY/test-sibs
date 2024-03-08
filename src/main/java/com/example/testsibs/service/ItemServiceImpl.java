package com.example.testsibs.service;

import com.example.testsibs.exception.InvalidFieldsException;
import com.example.testsibs.model.entity.Item;
import com.example.testsibs.repository.ItemRepository;
import javassist.NotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemServiceImpl extends LoggingService<Item> implements IItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public List<Item> list() {
        return itemRepository.findAll();
    }

    @Override
    public Item findById(Long id) throws NotFoundException {
        try {
            return itemRepository.findById(id).orElseThrow(() -> new NotFoundException("Item not found!"));
        } catch (NotFoundException ex) {
            logger.error(String.format("[ERROR] - %s", id), ex);
            throw new NotFoundException(ex.getMessage());
        } catch (Exception ex) {
            logger.error(String.format("[ERROR] - %s", id), ex);
            throw ex;
        }
    }

    @Override
    public Item create(Item item) throws InvalidFieldsException {
        return loggingCreate(() -> {
            item.setId(null);
            return itemRepository.save(item);
        }, item);
    }

    @Override
    public void update(Item item) throws NotFoundException, InvalidFieldsException {
        checkIfExists(item.getId());
        loggingCreate(() -> itemRepository.save(item), item);
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        try {
            checkIfExists(id);
            itemRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            if (ex.getCause() instanceof ConstraintViolationException) {
                logger.error("[ERROR] - Item already is in an order or a stock movement!");
                throw new IllegalArgumentException("It's not possible to complete this operation, item is in an order or a stock movement!");
            }
            logger.error("[ERROR]", ex);
            throw ex;
        }
    }

    private void checkIfExists(Long id) throws NotFoundException {
        if (!itemRepository.existsById(id)) {
            logger.error("[ERROR] - Item Not Found Exception " + id);
            throw new NotFoundException("Item not found!");
        }
    }
}
