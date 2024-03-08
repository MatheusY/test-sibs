package com.example.testsibs.service;

import com.example.testsibs.exception.InvalidFieldsException;
import com.example.testsibs.model.entity.User;
import com.example.testsibs.repository.UserRepository;
import javassist.NotFoundException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl extends LoggingService<User>  implements IUserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> list() {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long id) throws NotFoundException {
        try {
            return userRepository.findById(id).orElseThrow(() -> new NotFoundException("User not found!"));
        } catch (NotFoundException ex) {
            logger.error(String.format("[ERROR] - %s", id), ex);
            throw new NotFoundException(ex.getMessage());
        } catch (Exception ex) {
            logger.error(String.format("[ERROR] - %s", id), ex);
            throw ex;
        }
    }

    @Override
    public User create(User user) throws InvalidFieldsException {
        return loggingCreate(() -> {
            user.setId(null);
            return userRepository.save(user);
        }, user);
    }

    @Override
    public void update(User user) throws NotFoundException, InvalidFieldsException {
        checkIfExists(user.getId());
        loggingCreate(() -> userRepository.save(user), user);
    }

    @Override
    public void delete(Long id) throws NotFoundException {
        checkIfExists(id);
        try {
            userRepository.deleteById(id);
        } catch (DataIntegrityViolationException ex) {
            if(ex.getCause() instanceof ConstraintViolationException) {
                logger.error("[ERROR] - User already has an order!");
                throw new IllegalArgumentException("It's not possible to complete this operation, user has an order");
            }
            logger.error("[ERROR]", ex);
            throw ex;
        }
    }

    @Override
    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    private void checkIfExists(Long id) throws NotFoundException {
        if(!userRepository.existsById(id)) {
            logger.error("[ERROR] - User Not Found Exception " + id);
            throw new NotFoundException("User not found!");
        }
    }


}
