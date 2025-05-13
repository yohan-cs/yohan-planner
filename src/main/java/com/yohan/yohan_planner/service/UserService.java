package com.yohan.yohan_planner.service;

import com.yohan.yohan_planner.model.User;

public interface UserService {
    User createUser(User user);

    User getUserById(Long id);

    User getUserByUsername(String username);

    User updateUser(Long id, User updatedUser);

    void deleteUser(Long id);

    void enableUser(Long id);

    void disableUser(Long id);
}
