package com.mechanitis.undertest;

import java.util.Objects;
import java.util.stream.IntStream;

public enum MapreduceType {
    REPLACE,
    MERGE,
    REDUCE,
    INLINE;

    public static MapreduceType original(final String value) {
        for (int i = 0; i < values().length; i++) {
            final MapreduceType fo = values()[i];
            if (fo.name().equals(value)) {
                return fo;
            }
        }
        return null;
    }

    public static MapreduceType refactored(final String value) {
        return IntStream.range(0, values().length)
                        .mapToObj(i -> values()[i])
                        .filter(fo -> Objects.equals(fo.name(), value))
                        .findFirst()
                        .orElse(null);
    }

}
