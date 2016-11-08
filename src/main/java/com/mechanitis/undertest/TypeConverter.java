package com.mechanitis.undertest;

import java.util.Arrays;

public class TypeConverter {
    /**
     * checks if Class f is in classes *
     */
    public static boolean original(final Class f, final Class[] classes) {
        for (final Class c : classes) {
            if (c.equals(f)) {
                return true;
            }
        }
        return false;
    }

    public static boolean refactored(final Class f, final Class[] classes) {
        return Arrays.stream(classes)
                     .anyMatch(c -> c.equals(f));
    }

    public static boolean refactoredParallel(Class<?> f, Class[] classes) {
        return Arrays.stream(classes)
                     .parallel()
                     .anyMatch(c -> c.equals(f));
    }
}
