package com.mechanitis.undertest;

import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.annotations.Transient;
import org.mongodb.morphia.mapping.MappedClass;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.mapping.validation.ConstraintViolation;
import org.mongodb.morphia.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mongodb.morphia.utils.ReflectionUtils.getDeclaredAndInheritedFields;


public class ContainsEmbeddedWithId {
    public void checkOriginal(final Mapper mapper, final org.mongodb.morphia.mapping.MappedClass mc, final Set<ConstraintViolation> ve) {
        final Set<Class<?>> classesToInspect = new HashSet<Class<?>>();
        for (final Field field : getDeclaredAndInheritedFields(mc.getClazz(), true)) {
            if (isFieldToInspect(field) && !field.isAnnotationPresent(Id.class)) {
                classesToInspect.add(field.getType());
            }
        }
        checkRecursivelyHasNoIdAnnotationPresent(classesToInspect, new HashSet<Class<?>>(), mc, ve);
    }

    public void checkRefactored(final Mapper mapper, final org.mongodb.morphia.mapping.MappedClass mc, final Set<ConstraintViolation> ve) {
        final Set<Class<?>> classesToInspect = Arrays.stream(getDeclaredAndInheritedFields(mc.getClazz(), true))
                                                     .filter(field -> isFieldToInspect(field) && !field.isAnnotationPresent(Id.class))
                                                     .map(Field::getType)
                                                     .collect(Collectors.toSet());
        checkRecursivelyHasNoIdAnnotationPresent(classesToInspect, new HashSet<Class<?>>(), mc, ve);
    }

    private void checkRecursivelyHasNoIdAnnotationPresent(final Set<Class<?>> classesToInspect,
                                                          final HashSet<Class<?>> alreadyInspectedClasses, final MappedClass mc,
                                                          final Set<ConstraintViolation> ve) {
        for (final Class<?> clazz : classesToInspect) {
            if (alreadyInspectedClasses.contains(clazz)) {
                continue;
            }
            if (hasTypeFieldAnnotation(clazz, Id.class)) {
                ve.add(new ConstraintViolation(ConstraintViolation.Level.FATAL,
                        mc,
                        null,
                        "You cannot use @Id on any field of an Embedded/Property object"));
            }
            alreadyInspectedClasses.add(clazz);
            final Set<Class<?>> extraClassesToInspect = new HashSet<Class<?>>();
            for (final Field field : getDeclaredAndInheritedFields(clazz, true)) {
                if (isFieldToInspect(field)) {
                    extraClassesToInspect.add(field.getType());
                }
            }
            checkRecursivelyHasNoIdAnnotationPresent(extraClassesToInspect, alreadyInspectedClasses, mc, ve);
        }
    }

    private boolean hasTypeFieldAnnotation(final Class<?> type, final Class<Id> class1) {
        for (final Field field : getDeclaredAndInheritedFields(type, true)) {
            if (field.getAnnotation(class1) != null) {
                return true;
            }
        }
        return false;
    }

    private boolean isFieldToInspect(final Field field) {
        return (!field.isAnnotationPresent(Transient.class) && !field.isAnnotationPresent(Reference.class));
    }
}
