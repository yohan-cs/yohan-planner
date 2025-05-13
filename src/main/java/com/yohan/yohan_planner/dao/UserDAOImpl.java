package com.yohan.yohan_planner.dao;

import com.yohan.yohan_planner.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserDAOImpl implements UserDAO{

    private final EntityManager entityManager;

    @Autowired
    public UserDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<User> findAll() {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u", User.class);

        List<User> users = query.getResultList();

        return users;
    }

    @Override
    public Optional<User> findById(Long id) {
        User user = entityManager.find(User.class, id);
        return Optional.ofNullable(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        List<User> results = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();
        return results.stream().findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        List<User> results = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();
            return results.stream().findFirst();
    }


    @Override
    public List<User> findByRoleName(String roleName) {
        List<User> users = entityManager.createQuery(
                "SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName", User.class)
                .setParameter("roleName", roleName)
                .getResultList();
        return users;
    }

    @Override
    public User save(User user) {
        User updatedUser = entityManager.merge(user);

        return updatedUser;
    }

    @Override
    public void deleteById(Long id) {
        User user = entityManager.find(User.class, id);

        entityManager.remove(user);
    }
}
