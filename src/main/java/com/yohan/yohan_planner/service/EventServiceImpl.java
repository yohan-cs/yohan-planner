package com.yohan.yohan_planner.service;

import com.yohan.yohan_planner.exception.EventNotFoundException;
import com.yohan.yohan_planner.exception.InvalidTimeException;
import com.yohan.yohan_planner.model.Event;
import com.yohan.yohan_planner.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);
    private final EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    @Transactional
    public Event createEvent(Event event) {
        isStartBeforeEnd(event.getStartTime(), event.getEndTime());
        calculateDuration(event);
        logger.info("Created event: {}", event.getName());
        return eventRepository.save(event);
    }

    @Override
    public List<Event> getAllEvents() {
        logger.info("Fetching all events");
        List<Event> events = eventRepository.findAll();
        logger.info("Total events found: {}", events.size());
        return events;
    }

    @Override
    public Event getEventById(Long id) {
        logger.info("Fetching event by ID: {}", id);
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> {
                    return new EventNotFoundException(id);
                });
        logger.info("Event with ID {} found", id);
        return event;
    }

    @Override
    @Transactional
    public Event updateEvent(Long id, Event updatedEvent) {
        logger.info("Updating event with ID: {}", id);
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> {
                    return new EventNotFoundException(id);
                });

        if (updatedEvent.getName() != null) {
            if (updatedEvent.getName().isBlank()) {
                throw new IllegalArgumentException("Event name can not be blank.");
            }
            event.setName(updatedEvent.getName());
        }

        if (updatedEvent.getStartTime() != null || updatedEvent.getEndTime() != null) {
            updateStartEndTime(event, updatedEvent);
        }

        if (updatedEvent.getDescription() != null) {
            event.setDescription(updatedEvent.getDescription());
        }

        Event savedEvent = eventRepository.save(event);
        logger.info("Event with ID {} updated successfully", id);
        return savedEvent;
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        logger.info("Deleting event with ID: {}", id);

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> {
                    return new EventNotFoundException(id);
                });

        eventRepository.deleteById(id);
        logger.info("Event with ID {} deleted successfully", id);
    }

    private void updateStartEndTime(Event event, Event updatedEvent) {
        if (updatedEvent.getStartTime() != null && updatedEvent.getEndTime() != null) {
            // updated start time and end time
            if (isStartBeforeEnd(updatedEvent.getStartTime(), updatedEvent.getEndTime())) {
                event.setStartTime(updatedEvent.getStartTime());
                event.setEndTime(updatedEvent.getEndTime());
                calculateDuration(event);
                logger.info("Updated start time for event: {}", event.getName());
                logger.info("Updated end time for event: {}", event.getName());
            }
        } else if (updatedEvent.getStartTime() != null) {
            // updated start time only
            if (isStartBeforeEnd(updatedEvent.getStartTime(), event.getEndTime())) {
                event.setStartTime(updatedEvent.getStartTime());
                calculateDuration(event);
                logger.info("Updated start time for event: {}", event.getName());
            }
        } else if (updatedEvent.getEndTime() != null) {
            // updated end time only
            if (isStartBeforeEnd(event.getStartTime(), updatedEvent.getEndTime())) {
                event.setEndTime(updatedEvent.getEndTime());
                calculateDuration(event);
                logger.info("Updated end time for event: {}", event.getName());
            }
        }
    }

    private void calculateDuration(Event event) {
        logger.info("Calculating duration for event: {}", event.getName());

        long duration = Duration.between(event.getStartTime(), event.getEndTime()).toMinutes();
        event.setDuration(duration);

        logger.info("Duration for event {} is: {} minutes", event.getName(), duration);
    }

    private boolean isStartBeforeEnd(LocalDateTime startTime, LocalDateTime endTime) {
        logger.info("Validating start and end time");
        if (!startTime.isBefore(endTime)) {
            throw new InvalidTimeException(startTime, endTime);
        }
        logger.info("Start and end time are valid");
        return true;
    }
}
