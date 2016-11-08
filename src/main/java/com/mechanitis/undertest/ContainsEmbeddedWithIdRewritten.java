package com.mechanitis.undertest;

import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.annotations.Transient;
import org.mongodb.morphia.mapping.MappedClass;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.mapping.validation.ConstraintViolation;
import org.mongodb.morphia.utils.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;


public class ContainsEmbeddedWithIdRewritten {
    public void check(final Mapper mapper, final MappedClass mc, final Set<ConstraintViolation> ve) {
        final Set<Class<?>> classesToInspect = Arrays.stream(ReflectionUtils.getDeclaredAndInheritedFields(mc.getClazz(), true))
                                                     .filter(field -> isFieldToInspect(field) && !field.isAnnotationPresent(Id.class))
                                                     .map(Field::getType)
                                                     .collect(Collectors.toSet());
        checkRecursivelyHasNoIdAnnotationPresent(classesToInspect, new HashSet<Class<?>>(), mc, ve);
    }

    private void checkRecursivelyHasNoIdAnnotationPresent(final Set<Class<?>> classesToInspect,
                                                          final HashSet<Class<?>> alreadyInspectedClasses, final MappedClass mc,
                                                          final Set<ConstraintViolation> ve) {
        classesToInspect.stream()
                        .filter(clazz -> !alreadyInspectedClasses.contains(clazz))
                        .forEach(clazz -> {
                            if (hasTypeFieldAnnotation(clazz, Id.class)) {
                                ve.add(new ConstraintViolation(ConstraintViolation.Level.FATAL,
                                        mc,
                                        null,
                                        "You cannot use @Id on any field of an Embedded/Property object"));
                            }
                            alreadyInspectedClasses.add(clazz);
                            final Set<Class<?>> extraClassesToInspect = Arrays.stream(getDeclaredAndInheritedFields(clazz))
                                                                              .filter(this::isFieldToInspect)
                                                                              .map(Field::getType)
                                                                              .collect(Collectors.toSet());
                            checkRecursivelyHasNoIdAnnotationPresent(extraClassesToInspect, alreadyInspectedClasses, mc, ve);
                        });
    }

    private boolean hasTypeFieldAnnotation(final Class<?> type, final Class<Id> class1) {
        return Arrays.stream(getDeclaredAndInheritedFields(type))
                     .anyMatch(field -> field.getAnnotation(class1) != null);
    }

    private boolean isFieldToInspect(final Field field) {
        return (!field.isAnnotationPresent(Transient.class) && !field.isAnnotationPresent(Reference.class));
    }

    private static Field[] getDeclaredAndInheritedFields(final Class type) {
        final List<Field> allFields = new ArrayList<Field>();
        allFields.addAll(getValidFields(type.getDeclaredFields()));
        Class parent = type.getSuperclass();
        while ((parent != null) && (parent != Object.class)) {
            allFields.addAll(getValidFields(parent.getDeclaredFields()));
            parent = parent.getSuperclass();
        }
        return allFields.toArray(new Field[allFields.size()]);
    }

    private static List<Field> getValidFields(final Field[] fields) {
        return Arrays.stream(fields)
                     .filter(ContainsEmbeddedWithIdRewritten::isNotStatic)
                     .collect(Collectors.toList());
    }

    private static boolean isNotStatic(Field field) {
        return !Modifier.isStatic(field.getModifiers());
    }

}
