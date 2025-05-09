package com.yohan.yohan_planner.dao;

import com.yohan.yohan_planner.model.Day;
import com.yohan.yohan_planner.model.Event;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class DayDAOImpl implements DayDAO {

    private final EntityManager entityManager;

    @Autowired
    public DayDAOImpl(EntityManager entityManager) {

        this.entityManager = entityManager;
    }

    @Override
    public List<Day> findAll() {

        TypedQuery<Day> query = entityManager.createQuery("from Day", Day.class);

        return query.getResultList();
    }

    @Override
    public Optional<Day> findById(Long id) {

        Day day = entityManager.find(Day.class, id);

        return Optional.ofNullable(day);
    }

    @Override
    public Optional<Day> findByDate(LocalDate date) {
        TypedQuery<Day> query = entityManager.createQuery(
                "SELECT d FROM Day d WHERE d.date = :date", Day.class);
        query.setParameter("date", date);
        List<Day> results = query.getResultList();
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    @Override
    public Day save(Day day) {
        Day updatedDay = entityManager.merge(day);
        return updatedDay;
    }

    @Override
    public void deleteById(Long id) {
        Day day = entityManager.find(Day.class, id);
        if (day != null) {
            entityManager.remove(day);
        }
    }
}