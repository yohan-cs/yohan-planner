package com.yohan.yohan_planner.service;

import com.yohan.yohan_planner.dao.EventDAO;
import com.yohan.yohan_planner.exception.ConflictException;
import com.yohan.yohan_planner.exception.DayNotFoundException;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
        Day day = dayService.getOrCreateDay(event.getStartTime().toLocalDate());
        logger.info("Creating event: {}", event.getName());
        validateStartBeforeEnd(event.getStartTime(), event.getEndTime());
        validateNoConflicts(event.getStartTime(), event.getEndTime(), event);
        long duration = calculateDuration(event.getStartTime(), event.getEndTime());
        event.setDuration(duration);
        event.setDay(day);
        return saveEvent(event);
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
            // Determine the new start and end times without mutating the event
            LocalDateTime newStart = updatedEvent.getStartTime() != null
                    ? updatedEvent.getStartTime()
                    : event.getStartTime();

            LocalDateTime newEnd = updatedEvent.getEndTime() != null
                    ? updatedEvent.getEndTime()
                    : event.getEndTime();

            // Validate new times
            validateStartBeforeEnd(newStart, newEnd);

            // Validate no conflicts with other events (using the new time range)
            validateNoConflicts(newStart, newEnd, event);

            // Apply the new times
            event.setStartTime(newStart);
            event.setEndTime(newEnd);

            // Recalculate duration
            long duration = calculateDuration(event.getStartTime(), event.getEndTime());
            event.setDuration(duration);

            logger.info("Updated times and duration for event ID {}", id);
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

    private long calculateDuration(LocalDateTime startTime, LocalDateTime endTime) {

        long duration = Duration.between(startTime, endTime).toMinutes();

        logger.info("Duration is: {} minutes", duration);

        return duration;
    }

    private void validateStartBeforeEnd(LocalDateTime startTime, LocalDateTime endTime) {
        logger.info("Validating start and end time");
        if (!startTime.isBefore(endTime)) {
            throw new InvalidTimeException(startTime, endTime);
        }
        logger.info("Start and end time are valid");
    }

    // For event creation (no ID yet)
    private void validateNoConflicts(LocalDateTime startTime, LocalDateTime endTime) {
        logger.info("Checking for conflicts during creation (no event ID)");
        checkConflicts(startTime, endTime, null);
    }

    // For event update (exclude the current event's ID)
    private void validateNoConflicts(LocalDateTime startTime, LocalDateTime endTime, Event currentEvent) {
        logger.info("Checking for conflicts during update (event ID: {})", currentEvent.getId());
        checkConflicts(startTime, endTime, currentEvent.getId());
    }

    // Shared private method to handle conflict checking
    private void checkConflicts(LocalDateTime startTime, LocalDateTime endTime, Long excludeEventId) {
        Day day = dayService.getDayByDate(startTime.toLocalDate())
                .orElseThrow(() -> new DayNotFoundException(startTime.toLocalDate()));

        for (Event existing : day.getEvents()) {
            if (excludeEventId != null && Objects.equals(existing.getId(), excludeEventId)) {
                continue; // Skip checking against the current event during updates
            }

            if (existing.getStartTime().isBefore(endTime) &&
                    existing.getEndTime().isAfter(startTime)) {
                logger.error("Conflict found with existing event: {}", existing.getName());
                throw new ConflictException(existing);
            }
        }

        logger.info("No conflicts found");
    }
}
