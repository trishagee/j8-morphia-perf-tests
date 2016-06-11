package com.mechanitis.undertest;

import org.mongodb.morphia.Key;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class BasicDAO<T> {
    public List<?> keysToIdsOriginal(final List<Key<T>> keys) {
        final List<Object> ids = new ArrayList<>(keys.size() * 2);
        for (final Key<T> key : keys) {
            ids.add(key.getId());
        }
        return ids;
    }

    public List<?> keysToIdsSimplified(final List<Key<T>> keys) {
        final List<Object> ids = new ArrayList<>();
        for (final Key<T> key : keys) {
            ids.add(key.getId());
        }
        return ids;
    }

    public List<?> keysToIdsRefactored(final List<Key<T>> keys) {
        return keys.stream().map(Key::getId).collect(toList());
    }

}
