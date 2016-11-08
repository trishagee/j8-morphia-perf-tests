package com.mechanitis.undertest;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionUtils {
    public static List<Field> original(final Field[] fields, final boolean returnFinalFields) {
        final List<Field> validFields = new ArrayList<Field>();
        // we ignore static and final fields
        for (final Field field : fields) {
            if (!Modifier.isStatic(field.getModifiers()) && (returnFinalFields || !Modifier.isFinal(field.getModifiers()))) {
                validFields.add(field);
            }
        }
        return validFields;
    }

    public static List<Field> refactored(final Field[] fields, final boolean returnFinalFields) {
        return Arrays.stream(fields)
                     .filter(field -> isNotStaticOrFinal(returnFinalFields, field))
                     .collect(Collectors.toList());
    }

    private static boolean isNotStaticOrFinal(boolean returnFinalFields, Field field) {
        return !Modifier.isStatic(field.getModifiers()) && (returnFinalFields || !Modifier.isFinal(field.getModifiers()));
    }

}
