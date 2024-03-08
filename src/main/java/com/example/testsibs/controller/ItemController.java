package com.example.testsibs.controller;

import com.example.testsibs.views.dto.ItemDTO;
import com.example.testsibs.exception.InvalidFieldsException;
import com.example.testsibs.model.entity.Item;
import com.example.testsibs.service.IItemService;
import com.example.testsibs.views.vo.ItemVO;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/item")
public class ItemController extends AbstractController{

    @Autowired
    private IItemService itemService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ItemVO> findAll(){
        return convertList(itemService.list(), ItemVO.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ItemVO findById(@PathVariable Long id) throws NotFoundException {
        return convert(itemService.findById(id), ItemVO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createItem(@RequestBody ItemDTO itemDTO) throws InvalidFieldsException {
        return itemService.create(convert(itemDTO, Item.class)).getId();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody ItemDTO itemDTO) throws NotFoundException, InvalidFieldsException {
        Item item = convert(itemDTO, Item.class);
        item.setId(id);
        itemService.update(item);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id) throws NotFoundException {
        itemService.delete(id);
    }
}
