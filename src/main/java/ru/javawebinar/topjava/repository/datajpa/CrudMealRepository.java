package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;


@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:user_id")
    int deleteByIdAndUser(@Param("id") int id, @Param("user_id") int userId);

    @Query("SELECT m FROM Meal m WHERE m.user.id=:id ORDER BY m.dateTime DESC")
    List<Meal> getAllSortByUser(@Param("id") int userId);

    @Query("SELECT m FROM Meal m WHERE m.user.id=:user_id  AND m.dateTime >=:start AND m.dateTime <:end ORDER BY m.dateTime DESC")
    List<Meal> getBetweenHalfOpen(@Param("start") LocalDateTime startDateTime, @Param("end") LocalDateTime endDateTime, @Param("user_id") int userId);

    @Query("SELECT m FROM Meal m LEFT JOIN FETCH m.user WHERE m.id=:id AND m.user.id=:user_id")
    Meal getMealAndUser(@Param("id") int id, @Param("user_id") int userId);
}
