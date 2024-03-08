package com.example.testsibs.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractController {

    @Autowired
    private ObjectMapper mapper;

    protected <T> T convert(Object entity, Class<T> clazz){
        return mapper.convertValue(entity, clazz);
    }

    protected <T, E> List<T> convertList(List<E> entities, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        for (E entity: entities) {
            list.add(convert(entity, clazz));
        }
        return list;
    }
}
