package ru.javawebinar.topjava.service.datajpa;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
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
        Meal meal = service.getMealAndUser(MEAL1_ID, USER_ID);
        User user = meal.getUser();
        MEAL_MATCHER.assertMatch(meal, meal1);
        USER_MATCHER.assertMatch(user, UserTestData.user);
    }

    @Test
    public void getNotFoundMealAndUser() {
        assertThrows(NotFoundException.class, () -> service.getMealAndUser(MEAL1_ID, ADMIN_ID));
    }
}