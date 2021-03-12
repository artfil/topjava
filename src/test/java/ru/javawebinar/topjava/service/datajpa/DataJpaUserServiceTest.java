package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.UserServiceTest;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.Profiles.DATAJPA;
import static ru.javawebinar.topjava.UserTestData.USER_MATCHER;

@ActiveProfiles(profiles = DATAJPA)
public class DataJpaUserServiceTest extends UserServiceTest {
    @Test
    public void getUserAndMeals() {
        User user = service.getUserAndMeals(100000);
        List<Meal> meals = user.getMeals();
        USER_MATCHER.assertMatch(user, UserTestData.user);
        MEAL_MATCHER.assertMatch(meals, MealTestData.meals);

    }

    @Test
    public void getNotFoundUserAndMeals() {
        assertThrows(NotFoundException.class, () -> service.getUserAndMeals(NOT_FOUND));
    }
}