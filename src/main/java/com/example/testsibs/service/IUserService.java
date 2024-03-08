package com.example.testsibs.service;

import com.example.testsibs.model.entity.User;

public interface IUserService extends AbstractService<User>{

    User getUser(Long id);
}
