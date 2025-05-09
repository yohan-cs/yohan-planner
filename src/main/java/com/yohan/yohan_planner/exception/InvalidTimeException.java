package com.yohan.yohan_planner.exception;

import java.time.LocalDateTime;

public class InvalidTimeException extends RuntimeException {

    public InvalidTimeException(LocalDateTime startTime, LocalDateTime endTime) {
        super("Invalid event time: Start time " + startTime + " must be before end time " + endTime);
    }
}
