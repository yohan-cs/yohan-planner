package com.yohan.yohan_planner.controller;

import com.yohan.yohan_planner.model.User;
import com.yohan.yohan_planner.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Create a user (POST)
    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
        User createdUser = userService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    // Get user by id (GET)
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User user = userService.getUserById(id); // throws UserNotFoundException if not found
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Get user by username (GET)
    @GetMapping("/username/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        User user = userService.getUserByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    // Update a user (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<User> updateEvent(@PathVariable Long id, @RequestBody User updatedUser) {
        User savedUser = userService.updateUser(id, updatedUser);
        return ResponseEntity.ok(savedUser);
    }

    // Delete a user (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
