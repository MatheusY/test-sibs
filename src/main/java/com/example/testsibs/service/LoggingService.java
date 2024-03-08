package com.example.testsibs.service;

import com.example.testsibs.exception.InvalidFieldsException;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.function.Supplier;

public abstract class LoggingService<T> {
    protected static final Logger logger = LogManager.getLogger(LoggingService.class);

    @Autowired
    protected ObjectWriter objectWriter;

    @SneakyThrows
    protected T loggingCreate(Supplier<T> function, Object entity) {
        try {
            return function.get();
        } catch (DataIntegrityViolationException ex) {
            logger.error(String.format("[ERROR] - %s", objectWriter.writeValueAsString(entity)), ex);
            throw new InvalidFieldsException();
        } catch (Exception ex) {
            logger.error(String.format("[ERROR] - %s", objectWriter.writeValueAsString(entity)), ex);
            throw ex;
        }
    }

}
