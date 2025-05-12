package com.yohan.yohan_planner.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "days")
public class Day {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Primary Key for Day

    @NotNull(message = "Date cannot be null")
    @Column(nullable = false, unique = true)
    private LocalDate date; // Date for the Day

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private DayOfWeek dayOfWeek;

    // One-to-many relationship with Event (One Day can have multiple Events)
    @NotNull
    @OneToMany(mappedBy = "day")
    @JsonManagedReference
    private List<Event> events = new ArrayList<>();

    public Day() {
    }

    public Day(LocalDate date) {
        this.setDate(date);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
        this.dayOfWeek = date.getDayOfWeek();
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public List<Event> getEvents() {
        return events;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void addEvent(Event event) {
        events.add(event);
        event.setDay(this); // Ensure the event knows its parent day
    }

    public void removeEvent(Event event) {
        events.remove(event);
        event.setDay(null); // Disassociate the event from the day
    }


}
