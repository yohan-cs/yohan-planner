package com.yohan.yohan_planner.service;

import com.yohan.yohan_planner.dao.DayDAO;
import com.yohan.yohan_planner.exception.DayNotFoundException;
import com.yohan.yohan_planner.exception.EventNotFoundException;
import com.yohan.yohan_planner.model.Day;
import com.yohan.yohan_planner.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class DayServiceImpl implements DayService {

    private static final Logger logger = LoggerFactory.getLogger(DayServiceImpl.class);
    private final DayDAO dayDAO;

    @Autowired
    public DayServiceImpl(DayDAO dayDAO) {
        this.dayDAO = dayDAO;
    }

    @Override
    public List<Day> findAllDays() {
        logger.info("Fetching all days");
        List<Day> days = dayDAO.findAll();
        logger.info("Total days found: {}", days.size());
        return days;
    }

    @Override
    public Day getDayById(Long id) {
        logger.info("Fetching day by ID: {}", id);
        Day day = dayDAO.findById(id).orElseThrow(() -> new DayNotFoundException(id));
        logger.info("Day with ID {} found", id);
        return day;
    }

    @Override
    public Day getOrCreateDay(LocalDate date) {
        return getDayByDate(date).orElseGet(() -> save(new Day(date)));
    }

    @Override
    public Optional<Day> getDayByDate(LocalDate date) {
        logger.info("Fetching day by date: {}", date);
        return dayDAO.findByDate(date);
    }

    @Override
    public Day save(Day day) {
        return dayDAO.save(day);
    }
}