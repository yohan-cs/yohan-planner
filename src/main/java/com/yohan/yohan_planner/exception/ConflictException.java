package com.yohan.yohan_planner.exception;

import com.yohan.yohan_planner.model.Event;

public class ConflictException extends RuntimeException {

    public ConflictException(Event existingEvent) {
        super("This event conflicts with " + existingEvent.getName() +
                " - (" + existingEvent.getStartTime() + " - " + existingEvent.getEndTime() + ")");
    }

}