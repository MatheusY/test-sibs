package com.example.testsibs.controller;

import com.example.testsibs.views.dto.OrderDTO;
import com.example.testsibs.exception.InvalidFieldsException;
import com.example.testsibs.model.entity.Order;
import com.example.testsibs.service.IOrderService;
import com.example.testsibs.views.vo.OrderVO;
import io.swagger.annotations.SwaggerDefinition;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController extends AbstractController{

    @Autowired
    private IOrderService orderService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<OrderVO> findAll(){
        return convertList(orderService.list(), OrderVO.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderDTO findById(@PathVariable Long id) throws NotFoundException {
        return convert(orderService.findById(id), OrderDTO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createItem(@RequestBody OrderDTO orderDTO) throws InvalidFieldsException {
        return orderService.create(convert(orderDTO, Order.class)).getId();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody OrderDTO orderDTO) throws NotFoundException, InvalidFieldsException {
        Order order = convert(orderDTO, Order.class);
        order.setId(id);
        orderService.update(order);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id) throws NotFoundException {
        orderService.delete(id);
    }

    @GetMapping("/stockMovement/{id}")
    @ResponseStatus(HttpStatus.OK)
    public List<OrderVO> getByStockMovement(@PathVariable Long id) {
        return convertList(orderService.findByStockMovement(id), OrderVO.class);
    }
}
