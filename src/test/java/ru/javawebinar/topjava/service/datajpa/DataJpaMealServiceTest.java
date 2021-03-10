package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.MealServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(profiles = DATAJPA)
public class DataJpaMealServiceTest extends MealServiceTest {
    @Test
    public void getMealAndUser() {
        MEAL_MATCHER.assertMatch(service.getMealAndUser(MEAL1_ID, USER_ID), meal1);
        USER_MATCHER.assertMatch(service.getMealAndUser(ADMIN_MEAL_ID, ADMIN_ID).getUser(), admin);
    }

    @Test
    public void getNotFoundMealAndUser() {
        assertThrows(NotFoundException.class,()->service.getMealAndUser(MEAL1_ID,ADMIN_ID));
    }
}