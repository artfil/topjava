package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_USER_ID1 = START_SEQ + 2;
    public static final int MEAL_USER_ID2 = START_SEQ + 3;
    public static final int MEAL_USER_ID3 = START_SEQ + 4;
    public static final int MEAL_USER_ID4 = START_SEQ + 5;
    public static final int MEAL_USER_ID5 = START_SEQ + 6;
    public static final int MEAL_USER_ID6 = START_SEQ + 7;
    public static final LocalDate START_DATE = LocalDate.of(2021, Month.FEBRUARY, 24);
    public static final LocalDate END_DATE = LocalDate.of(2021, Month.FEBRUARY, 28);

    public static final Meal meal1 = new Meal(
            MEAL_USER_ID1, LocalDateTime.of(2021, Month.FEBRUARY, 27, 12, 34), "Завтрак", 500);
    public static final Meal meal2 = new Meal(
            MEAL_USER_ID2, LocalDateTime.of(2021, Month.FEBRUARY, 27, 15, 34), "Обед", 1000);
    public static final Meal meal3 = new Meal(
            MEAL_USER_ID3, LocalDateTime.of(2021, Month.FEBRUARY, 27, 21, 34), "Ужин", 500);
    public static final Meal meal4 = new Meal(
            MEAL_USER_ID4, LocalDateTime.of(2021, Month.FEBRUARY, 27, 12, 34), "Завтрак", 1000);
    public static final Meal meal5 = new Meal(
            MEAL_USER_ID5, LocalDateTime.of(2021, Month.FEBRUARY, 27, 15, 34), "Обед", 1000);
    public static final Meal meal6 = new Meal(
            MEAL_USER_ID6, LocalDateTime.of(2021, Month.FEBRUARY, 27, 22, 34), "Ужин", 500);

    public static Meal getNew() {
        return new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "New meal", 1000);
    }

    public static Meal getUpdated() {
        Meal updated = new Meal(meal4);
        updated.setDescription("Updated meal");
        updated.setCalories(500);
        updated.setDateTime(LocalDateTime.of(2021, Month.FEBRUARY, 20, 12, 34));
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingFieldByFieldElementComparator().isEqualTo(expected);
    }
}