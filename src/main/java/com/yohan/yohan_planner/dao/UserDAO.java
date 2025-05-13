package com.yohan.yohan_planner.dao;

import com.yohan.yohan_planner.model.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    List<User> findAll();
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByRoleName(String roleName);
    User save(User user);
    void deleteById(Long id);
}