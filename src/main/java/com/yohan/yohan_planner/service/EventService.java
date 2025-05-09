package com.yohan.yohan_planner.service;

import com.yohan.yohan_planner.model.Event;

import java.util.List;

public interface EventService {
    Event createEvent(Event event);
    List<Event> getAllEvents();
    Event getEventById(Long id);
    Event updateEvent(Long id, Event updatedEvent);
    void deleteEvent(Long id);
}
