package com.yohan.yohan_planner.exception;

import java.time.LocalDateTime;

public class InvalidTimeException extends RuntimeException {

    public InvalidTimeException(LocalDateTime startTime, LocalDateTime endTime) {
        super("Start time must be before before end time - " +
                "Start time : " + startTime + " ; End time : " + endTime);
    }
}
