package com.yohan.yohan_planner.controller;

import com.yohan.yohan_planner.model.Event;
import com.yohan.yohan_planner.service.EventService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService eventService;
    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    // Create an event (POST)
    @PostMapping
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event newEvent) {
        Event savedEvent = eventService.createEvent(newEvent);
        return new ResponseEntity<>(savedEvent, HttpStatus.CREATED);
    }

    // Get all events (GET)
    @GetMapping
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.getAllEvents();
        return new ResponseEntity<>(events, HttpStatus.OK);
    }

    // Get event by id (GET)
    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Event event = eventService.getEventById(id);

        return new ResponseEntity<>(event, HttpStatus.OK);
    }

    // Update an event (PATCH)
    @PatchMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event updatedEvent) {
        Event savedEvent = eventService.updateEvent(id, updatedEvent);
        return ResponseEntity.ok(savedEvent);
    }

    // Delete an event (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }
}
