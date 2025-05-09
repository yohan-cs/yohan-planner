package com.yohan.yohan_planner.repository;

import com.yohan.yohan_planner.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
