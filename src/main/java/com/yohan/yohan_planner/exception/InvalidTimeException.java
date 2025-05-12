package com.yohan.yohan_planner.exception;

import java.time.ZonedDateTime;

public class InvalidTimeException extends RuntimeException {

    public InvalidTimeException(ZonedDateTime startTime, ZonedDateTime endTime) {
        super("Invalid event time: Start time " + startTime + " must be before end time " + endTime);
    }
}
