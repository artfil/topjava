package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.service.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;
import static ru.javawebinar.topjava.UserTestData.admin;

@ActiveProfiles(profiles = DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {
    @Test
    public void getUserAndMeals() {
        MEAL_MATCHER.assertMatch(service.getUserAndMeals(100000).getMeals(), meals);
        USER_MATCHER.assertMatch(service.getUserAndMeals(100001), admin);
    }

    @Test
    public void getNotFoundUserAndMeals() {
        assertThrows(NotFoundException.class, () -> service.getUserAndMeals(NOT_FOUND));
    }
}