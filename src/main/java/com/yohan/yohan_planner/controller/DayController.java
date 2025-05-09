package com.yohan.yohan_planner.controller;

import com.yohan.yohan_planner.model.Day;
import com.yohan.yohan_planner.model.Event;
import com.yohan.yohan_planner.service.DayService;
import com.yohan.yohan_planner.service.EventService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/days")
public class DayController {

    private final DayService dayService;

    @Autowired
    public DayController(DayService dayService) {
        this.dayService = dayService;
    }


    // Get Day by ID
    @GetMapping("/{id}")
    public ResponseEntity<Day> getDayById(@PathVariable Long id) {
        Day day = dayService.getDayById(id);  // Throws if not found
        return new ResponseEntity<>(day, HttpStatus.OK);
    }

}
