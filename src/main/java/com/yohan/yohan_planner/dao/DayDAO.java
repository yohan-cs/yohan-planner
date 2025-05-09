package com.yohan.yohan_planner.dao;

import com.yohan.yohan_planner.model.Day;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DayDAO {

    List<Day> findAll();
    Optional<Day> findById(Long id);
    Optional<Day> findByDate(LocalDate date);
    Day save(Day day);
    void deleteById(Long id);
}