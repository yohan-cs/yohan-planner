package com.yohan.yohan_planner.service;

import com.yohan.yohan_planner.model.Day;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface DayService {
    List<Day> findAllDays();
    Day getDayById(Long id);
    Day getOrCreateDay(ZonedDateTime startTime);
    Optional<Day> getDayByDate(LocalDate date);
    Day save(Day day);
}