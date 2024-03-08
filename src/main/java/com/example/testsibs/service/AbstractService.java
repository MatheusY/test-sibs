package com.example.testsibs.service;

import com.example.testsibs.exception.InvalidFieldsException;
import com.example.testsibs.model.entity.Item;
import com.fasterxml.jackson.core.JsonProcessingException;
import javassist.NotFoundException;

import java.util.List;

public interface AbstractService<E> {

    List<E> list();

    E findById(Long id) throws NotFoundException;

    E create(E entity) throws InvalidFieldsException;

    void update(E entity) throws NotFoundException, InvalidFieldsException;

    void delete(Long id) throws NotFoundException;
}
