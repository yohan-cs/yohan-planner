package com.yohan.yohan_planner.dao;

import com.yohan.yohan_planner.model.Event;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class EventDAOImpl implements EventDAO {

    private final EntityManager entityManager;

    @Autowired
    public EventDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<Event> findAll() {

        TypedQuery<Event> query = entityManager.createQuery("from Event", Event.class);

        List<Event> events = query.getResultList();

        return events;
    }

    @Override
    public Optional<Event> findById(Long id) {

        Event event = entityManager.find(Event.class, id);

        return Optional.ofNullable(event);
    }

    @Override
    public Event save(Event event) {

        Event updatedEvent = entityManager.merge(event);

        return updatedEvent;
    }

    @Override
    public void deleteById(Long id) {

        Event event = entityManager.find(Event.class, id);

        entityManager.remove(event);
    }
}
