package com.mechanitis.undertest;

import org.mongodb.morphia.mapping.MappedClass;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.mapping.validation.ClassConstraint;
import org.mongodb.morphia.mapping.validation.ConstraintViolation;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class DuplicatedAttributeNames {
    public void originalIteration(final Mapper mapper, final MappedClass mc, final Set<ConstraintViolation> ve) {
        final Set<String> foundNames = new HashSet<String>();
        for (final MappedField mappedField : mc.getPersistenceFields()) {
            for (final String name : mappedField.getLoadNames()) {
                if (!foundNames.add(name)) {
                    //no noticable difference between .class and getClass
                    ve.add(new ConstraintViolation(ConstraintViolation.Level.FATAL, mc, mappedField, ClassConstraint.class,
                            "Mapping to MongoDB field name '" + name
                                    + "' is duplicated; you cannot map different java fields to the same MongoDB field."));
                }
            }
        }
    }

    public void refactoredIteration(final Mapper mapper, final MappedClass mc, final Set<ConstraintViolation> ve) {
        final Set<String> foundNames = new HashSet<>();
        for (final MappedField mappedField : mc.getPersistenceFields()) {
            //no noticable difference between .class and getClass
            ve.addAll(mappedField.getLoadNames().stream()
                                 .filter(name -> !foundNames.add(name))
                                 .map(name -> new ConstraintViolation(ConstraintViolation.Level.FATAL, mc, mappedField, ClassConstraint.class,
                                         "Mapping to MongoDB field name '" + name
                                                 + "' is duplicated; you cannot map different java fields to the same MongoDB field."))
                                 .collect(toList()));
        }
    }

}
