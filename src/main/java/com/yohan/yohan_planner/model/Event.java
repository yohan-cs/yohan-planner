package com.yohan.yohan_planner.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.ZonedDateTime;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Event name can not be blank")
    @Column(nullable = false, length = 50)
    private String name; // Name of event

    @NotNull(message = "Start time is required")
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime startTime; // Time of event start

    @NotNull(message = "End time is required")
    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    private ZonedDateTime endTime; // Time of event end

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private long duration; // Duration of event

    @Size(max = 255, message = "Description must be less than 255 characters")
    private String description; // Description of event

    @ManyToOne
    @JoinColumn(name = "day_id") // Foreign key to the Day entity
    @JsonBackReference
    private Day day; // Day associated with this event

    public Event() {
    }

    public Event(String name, ZonedDateTime startTime, ZonedDateTime endTime, String description) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public long getDuration() {
        return duration;
    }

    public String getDescription() {
        return description;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                ", description='" + description + '\'' +
                ", day=" + (day != null ? day.getDate() + " (" + day.getDayOfWeek() + ")" : "No day assigned") +
                '}';
    }
}
