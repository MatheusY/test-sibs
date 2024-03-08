package com.example.testsibs.controller;

import com.example.testsibs.views.dto.StockMovementDTO;
import com.example.testsibs.exception.InvalidFieldsException;
import com.example.testsibs.model.entity.StockMovement;
import com.example.testsibs.service.IStockMovementService;
import com.example.testsibs.views.vo.StockMovementVO;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stockMovement")
public class StockMovementController extends AbstractController{

    @Autowired
    private IStockMovementService stockMovementService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<StockMovementDTO> findAll(){
        return convertList(stockMovementService.list(), StockMovementDTO.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public StockMovementVO findById(@PathVariable Long id) throws NotFoundException {
        return convert(stockMovementService.findById(id), StockMovementVO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createItem(@RequestBody StockMovementDTO stockMovementDTO) throws InvalidFieldsException {
        return stockMovementService.create(convert(stockMovementDTO, StockMovement.class)).getId();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody StockMovementDTO stockMovementDTO) throws NotFoundException, InvalidFieldsException {
        StockMovement stockMovement = convert(stockMovementDTO, StockMovement.class);
        stockMovement.setId(id);
        stockMovementService.update(stockMovement);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id) throws NotFoundException {
        stockMovementService.delete(id);
    }

    @GetMapping("/order/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<StockMovementVO> getByOrder(@PathVariable Long id) {
        return convertList(stockMovementService.findByOrder(id), StockMovementVO.class);
    }
}
