package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.ArrayList;
import java.util.List;

public class ListStorage implements Storage {
    private final List<Meal> listStorage = new ArrayList<>();

    @Override
    public void add(Meal meal) {
        listStorage.add(meal);
    }

    @Override
    public Meal get(String uuid) {
        if (isExist(uuid)) {
            return null;
        }
        return listStorage.get(getIndex(uuid));
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(listStorage);
    }

    @Override
    public void update(Meal meal) {
        if (isExist(meal.getUuid())) {
            throw new RuntimeException();
        }
        listStorage.set(getIndex(meal.getUuid()), meal);
    }

    @Override
    public void delete(String uuid) {
        if (isExist(uuid)) {
            throw new RuntimeException();
        }
        listStorage.remove(getIndex(uuid));
    }

    @Override
    public void clear() {
        listStorage.clear();
    }

    @Override
    public int size() {
        return listStorage.size();
    }

    private boolean isExist(String uuid) {
        return getIndex(uuid) == -1;
    }

    private int getIndex(String uuid) {
        for (int i = 0; i < listStorage.size(); i++) {
            if (listStorage.get(i).getUuid().equals(uuid)) {
                return i;
            }
        }
        return -1;
    }
}
