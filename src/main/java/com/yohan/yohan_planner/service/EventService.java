package com.yohan.yohan_planner.service;

import com.yohan.yohan_planner.model.Event;

import java.time.LocalDate;
import java.util.List;

public interface EventService {
    Event createEvent(Event event);
    List<Event> getAllEvents();
    Event getEventById(Long id);
    Event updateEvent(Long id, Event updatedEvent);
    List<Event> getEventsByDate(LocalDate date);
    void deleteEvent(Long id);
}
