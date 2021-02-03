package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentHashMapStorage implements Storage {
    private static final Map<Integer, Meal> concurrentHashMap = new ConcurrentHashMap<>();
    private static final AtomicInteger count = new AtomicInteger(0);

    @Override
    public void add(Meal meal) {
        meal.setId(count.incrementAndGet());
        concurrentHashMap.put(meal.getId(), meal);
    }

    @Override
    public Meal get(int id) {
        return concurrentHashMap.get(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(concurrentHashMap.values());
    }

    @Override
    public void update(int id, Meal meal) {
        concurrentHashMap.merge(id, meal, (meal1, meal2) -> meal2);
    }

    @Override
    public void delete(int id) {
        concurrentHashMap.remove(id);
    }
}
