package com.example.testsibs.controller;

import com.example.testsibs.views.dto.UserDTO;
import com.example.testsibs.exception.InvalidFieldsException;
import com.example.testsibs.model.entity.User;
import com.example.testsibs.service.IUserService;
import com.example.testsibs.views.vo.UserVO;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController extends AbstractController{

    @Autowired
    private IUserService userService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<UserVO> findAll(){
        return convertList(userService.list(), UserVO.class);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserVO findById(@PathVariable Long id) throws NotFoundException {
        return convert(userService.findById(id), UserVO.class);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Long createItem(@RequestBody UserDTO userDTO) throws InvalidFieldsException {
        return userService.create(convert(userDTO, User.class)).getId();
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody UserDTO userDTO) throws NotFoundException, InvalidFieldsException {
        User user = convert(userDTO, User.class);
        user.setId(id);
        userService.update(user);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id) throws NotFoundException {
        userService.delete(id);
    }
}
