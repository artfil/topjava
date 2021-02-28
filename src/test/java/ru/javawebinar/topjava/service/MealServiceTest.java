package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void strangerMeal() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(MealTestData.MEAL_ID5, UserTestData.USER_ID));
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.MEAL_ID5, UserTestData.USER_ID));
        assertThrows(NotFoundException.class, () -> service.update(MealTestData.getUpdated(), UserTestData.USER_ID));
    }

    @Test
    public void duplicateDateTimeCreate() throws Exception {
        assertThrows(DataAccessException.class, () -> service.create(new Meal(
                LocalDateTime.of(2021, Month.FEBRUARY, 27, 12, 34), "Завтрак", 500), UserTestData.USER_ID));
    }

    @Test
    public void create() throws Exception {
        Meal newMeal = MealTestData.getNew();
        Meal created = service.create(newMeal, UserTestData.USER_ID);
        Integer newId = created.getId();
        newMeal.setId(newId);
        MealTestData.assertMatch(created, newMeal);
        MealTestData.assertMatch(service.get(newId, UserTestData.USER_ID), newMeal);
    }

    @Test
    public void get() throws Exception {
        Meal meal = service.get(MealTestData.MEAL_ID6, UserTestData.ADMIN_ID);
        MealTestData.assertMatch(meal, meal);
    }

    @Test
    public void delete() throws Exception {
        service.delete(MealTestData.MEAL_ID1, UserTestData.USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MealTestData.MEAL_ID1, UserTestData.USER_ID));
    }

    @Test
    public void getBetweenInclusive() throws Exception {
        MealTestData.assertMatch(service.getBetweenInclusive(MealTestData.START_DATE, MealTestData.END_DATE, UserTestData.ADMIN_ID), MealTestData.meal6, MealTestData.meal5, MealTestData.meal4);
        service.update(MealTestData.getUpdated(), UserTestData.ADMIN_ID);
        MealTestData.assertMatch(service.getBetweenInclusive(MealTestData.START_DATE, MealTestData.END_DATE, UserTestData.ADMIN_ID), MealTestData.meal6, MealTestData.meal5);
    }

    @Test
    public void getAll() throws Exception {
        List<Meal> all = service.getAll(UserTestData.ADMIN_ID);
        MealTestData.assertMatch(all, MealTestData.meal6, MealTestData.meal5, MealTestData.meal4);
    }

    @Test
    public void update() throws Exception {
        Meal update = MealTestData.getUpdated();
        service.update(update, UserTestData.ADMIN_ID);
        MealTestData.assertMatch(service.get(MealTestData.MEAL_ID4, UserTestData.ADMIN_ID), update);
    }
}