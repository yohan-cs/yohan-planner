package com.yohan.yohan_planner.service;

import com.yohan.yohan_planner.dao.EventDAO;
import com.yohan.yohan_planner.exception.EventNotFoundException;
import com.yohan.yohan_planner.exception.InvalidTimeException;
import com.yohan.yohan_planner.model.Day;
import com.yohan.yohan_planner.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);
    private final EventDAO eventDAO;
    private final DayService dayService;

    @Autowired
    public EventServiceImpl(EventDAO eventDAO, DayService dayService) {
        this.eventDAO = eventDAO;
        this.dayService = dayService;
    }

    @Override
    public List<Event> getAllEvents() {
        logger.info("Fetching all events");
        List<Event> events = eventDAO.findAll();
        logger.info("Total events found: {}", events.size());
        return events;
    }

    @Override
    public Event getEventById(Long id) {
        logger.info("Fetching event by ID: {}", id);
        Event event = eventDAO.findById(id).orElseThrow(() -> new EventNotFoundException(id));
        logger.info("Event with ID {} found", id);
        return event;
    }

    @Override
    @Transactional
    public Event createEvent(Event event) {
        Day day = getOrCreateDay(event.getStartTime().toLocalDate());
        logger.info("Creating event: {}", event.getName());
        validateStartBeforeEnd(event.getStartTime(), event.getEndTime());
        long duration = calculateDuration(event);
        event.setDuration(duration);
        event.setDay(day);
        return saveEvent(event);
    }

    private Day getOrCreateDay(LocalDate date) {
        return dayService.getDayByDate(date).orElseGet(() -> {
            Day newDay = new Day(date);
            return dayService.save(newDay);
        });
    }

    @Override
    @Transactional
    public void deleteEvent(Long id) {
        logger.info("Deleting event with ID: {}", id);

        Event event = eventDAO.findById(id).orElseThrow(() -> new EventNotFoundException(id));

        eventDAO.deleteById(id);
        logger.info("Event with ID {} deleted successfully", id);
    }

    @Override
    @Transactional
    public Event updateEvent(Long id, Event updatedEvent) {
        logger.info("Updating event with ID: {}", id);
        Event event = eventDAO.findById(id).orElseThrow(() -> new EventNotFoundException(id));
        if (updatedEvent.getName() != null) {
            if (updatedEvent.getName().isBlank()) {
                throw new IllegalArgumentException("Event name can not be blank.");
            }
            event.setName(updatedEvent.getName());
        }

        if (updatedEvent.getStartTime() != null || updatedEvent.getEndTime() != null) {
            updateStartAndEndTime(event, updatedEvent);
            long duration = calculateDuration(event);
            event.setDuration(duration);
        }

        if (updatedEvent.getDescription() != null) {
            event.setDescription(updatedEvent.getDescription());
        }


        return saveEvent(event);
    }

    private Event saveEvent(Event event) {
        logger.info("Saving event: {}", event.getName());
        Event saved = eventDAO.save(event);
        logger.info("Event saved with ID: {}", saved.getId());
        return saved;
    }

    private void updateStartAndEndTime(Event event, Event updatedEvent) {
        if (updatedEvent.getStartTime() != null && updatedEvent.getEndTime() != null) {
            // updated start and end time
            validateStartBeforeEnd(updatedEvent.getStartTime(), updatedEvent.getEndTime());
            event.setStartTime(updatedEvent.getStartTime());
            event.setEndTime(updatedEvent.getEndTime());
            logger.info("Updated start and end time for event: {}", event.getName());
        } else if (updatedEvent.getStartTime() != null) {
            // updated start time
            validateStartBeforeEnd(updatedEvent.getStartTime(), event.getEndTime());
            event.setStartTime(updatedEvent.getStartTime());
            logger.info("Updated start time for event: {}", event.getName());
        } else if (updatedEvent.getEndTime() != null) {
            // updated end time
            validateStartBeforeEnd(event.getStartTime(), updatedEvent.getEndTime());
            event.setEndTime(updatedEvent.getEndTime());
            logger.info("Updated end time for event: {}", event.getName());
        }
    }

    private long calculateDuration(Event event) {
        logger.info("Calculating duration for event: {}", event.getName());

        long duration = Duration.between(event.getStartTime(), event.getEndTime()).toMinutes();

        logger.info("Duration for event {} is: {} minutes", event.getName(), duration);

        return duration;
    }

    private void validateStartBeforeEnd(LocalDateTime startTime, LocalDateTime endTime) {
        logger.info("Validating start and end time");
        if (!startTime.isBefore(endTime)) {
            throw new InvalidTimeException(startTime, endTime);
        }
        logger.info("Start and end time are valid");
    }
}
