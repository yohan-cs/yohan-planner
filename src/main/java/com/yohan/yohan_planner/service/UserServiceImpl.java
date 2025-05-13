package com.yohan.yohan_planner.service;

import com.yohan.yohan_planner.dao.UserDAO;
import com.yohan.yohan_planner.exception.UserNotFoundException;
import com.yohan.yohan_planner.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DateTimeException;
import java.time.ZoneId;

@Service
public class UserServiceImpl implements UserService{

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserDAO userDAO;

    @Autowired
    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @Transactional
    public User createUser(User user) {
        logger.info("Creating user with username: {}", user.getUsername());
        // Check if user with the same username already exists
        if (userDAO.findByUsername(user.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already taken");
        }

        User savedUser = userDAO.save(user);
        logger.info("User with ID {} created successfully", savedUser.getId());
        return savedUser;
    }

    @Override
    public User getUserById(Long id) {
        logger.info("Fetching user by ID: {}", id);
        User user = userDAO.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        logger.info("User with ID {} found", id);
        return user;
    }

    @Override
    public User getUserByUsername(String username) {
        logger.info("Fetching user with username: {}", username);
        User user = userDAO.findByUsername(username).orElseThrow(() -> new UserNotFoundException(username));
        logger.info("User with username {} found", username);
        return user;
    }

    @Override
    @Transactional
    public User updateUser(Long id, User updatedUser) {
        logger.info("Updating user with ID: {}", id);
        User user = userDAO.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        // Update username
        if (updatedUser.getUsername() != null) {
            if (updatedUser.getFirstName().isBlank()) {
                throw new IllegalArgumentException("Username can not be blank.");
            }
            // Check for username conflict
            userDAO.findByUsername(updatedUser.getUsername())
                    .filter(conflict -> !conflict.getId().equals(id))
                    .ifPresent(conflict -> {
                        throw new IllegalArgumentException("Username already taken");
                    });
            user.setUsername(updatedUser.getUsername());
        }

        // Update first name
        if (updatedUser.getFirstName() != null) {
            if (updatedUser.getFirstName().isBlank()) {
                throw new IllegalArgumentException("First name can not be blank.");
            }
            user.setFirstName(updatedUser.getFirstName());
        }

        // Update last name
        if (updatedUser.getLastName() != null) {
            if (updatedUser.getLastName().isBlank()) {
                throw new IllegalArgumentException("Last name can not be blank.");
            }
            user.setLastName(updatedUser.getLastName());
        }

        // Update email
        if (updatedUser.getEmail() != null) {
            if (updatedUser.getEmail().isBlank()) {
                throw new IllegalArgumentException("Email cannot be blank.");
            }
            userDAO.findByEmail(updatedUser.getEmail())
                    .filter(conflict -> !conflict.getId().equals(id))
                    .ifPresent(conflict -> {
                        throw new IllegalArgumentException("Email already in use");
                    });
            user.setEmail(updatedUser.getEmail());
        }

        // Update timezone
        if (updatedUser.getTimezone() != null) {
            if (updatedUser.getTimezone().isBlank()) {
                throw new IllegalArgumentException("Timezone cannot be blank.");
            }
            validateTimezone(updatedUser.getTimezone());
            user.setTimezone(updatedUser.getTimezone());
        }

        User savedUser = userDAO.save(user);
        logger.info("User with ID {} updated successfully", savedUser.getId());

        return savedUser;
    }

    @Override
    public void deleteUser(Long id) {
        logger.info("Deleting event with ID: {}", id);

        User user = userDAO.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        userDAO.deleteById(id);
        logger.info("User with ID {} deleted successfully", id);
    }

    @Override
    public void enableUser(Long id) {
        User user = getUserById(id);
        user.setEnabled(true);
        logger.info("User with ID {} enabled successfully", id);
    }

    @Override
    public void disableUser(Long id) {
        User user = getUserById(id);
        user.setEnabled(false);
        logger.info("User with ID {} disabled successfully", id);
    }

    private void validateTimezone(String timezone) {
        try {
            ZoneId.of(timezone);
        } catch (DateTimeException ex) {
            throw new IllegalArgumentException("Invalid timezone: " + timezone);
        }
    }
}
