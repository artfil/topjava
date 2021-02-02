package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Storage {
    void add(Meal meal);

    Meal get(String uuid);

    List<Meal> getAll();

    void update(Meal meal);

    void delete(String uuid);

    void clear();

    int size();
}
