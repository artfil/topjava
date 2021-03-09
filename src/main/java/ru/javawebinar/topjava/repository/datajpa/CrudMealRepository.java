package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.time.LocalDateTime;
import java.util.List;


@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    Meal getByIdAndUser(int id, User user);

    @Transactional
    int deleteByIdAndUser(int id, User user);

    List<Meal> getAllSortByUser(User user, Sort sort);

    @Modifying
    @Query("SELECT m FROM Meal m WHERE m.user.id=:user_id  AND m.dateTime >=:start AND m.dateTime <:end ORDER BY m.dateTime DESC")
    List<Meal> getBetweenHalfOpen(@Param("start") LocalDateTime startDateTime, @Param("end") LocalDateTime endDateTime, @Param("user_id") int userId);
}
