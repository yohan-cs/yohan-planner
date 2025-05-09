package com.yohan.yohan_planner.dao;

import com.yohan.yohan_planner.model.Event;

import java.util.List;
import java.util.Optional;

public interface EventDAO {

    List<Event> findAll();
    Optional<Event> findById(Long id);
    Event save(Event event);
    void deleteById(Long id);
}
