package com.mechanitis.undertest;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.annotations.Serialized;
import org.mongodb.morphia.logging.Logger;
import org.mongodb.morphia.logging.MorphiaLoggerFactory;
import org.mongodb.morphia.mapping.MappedClass;
import org.mongodb.morphia.mapping.Mapper;
import org.mongodb.morphia.mapping.validation.ClassConstraint;
import org.mongodb.morphia.mapping.validation.ConstraintViolation;
import org.mongodb.morphia.mapping.validation.ConstraintViolationException;
import org.mongodb.morphia.mapping.validation.classrules.EmbeddedAndId;
import org.mongodb.morphia.mapping.validation.classrules.EmbeddedAndValue;
import org.mongodb.morphia.mapping.validation.classrules.EntityAndEmbed;
import org.mongodb.morphia.mapping.validation.classrules.EntityCannotBeMapOrIterable;
import org.mongodb.morphia.mapping.validation.classrules.MultipleId;
import org.mongodb.morphia.mapping.validation.classrules.MultipleVersions;
import org.mongodb.morphia.mapping.validation.classrules.NoId;
import org.mongodb.morphia.mapping.validation.fieldrules.ContradictingFieldAnnotation;
import org.mongodb.morphia.mapping.validation.fieldrules.LazyReferenceMissingDependencies;
import org.mongodb.morphia.mapping.validation.fieldrules.LazyReferenceOnArray;
import org.mongodb.morphia.mapping.validation.fieldrules.MapKeyDifferentFromString;
import org.mongodb.morphia.mapping.validation.fieldrules.MapNotSerializable;
import org.mongodb.morphia.mapping.validation.fieldrules.MisplacedProperty;
import org.mongodb.morphia.mapping.validation.fieldrules.ReferenceToUnidentifiable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static java.util.Collections.sort;

public class MappingValidator {
    private static final Logger LOG = MorphiaLoggerFactory.get(MappingValidator.class);

    public static void validateOriginal(final Mapper mapper, final List<MappedClass> classes) {
        final Set<ConstraintViolation> ve = new TreeSet<ConstraintViolation>(new Comparator<ConstraintViolation>() {

            @Override
            public int compare(final ConstraintViolation o1, final ConstraintViolation o2) {
                return o1.getLevel().ordinal() > o2.getLevel().ordinal() ? -1 : 1;
            }
        });

        final List<ClassConstraint> rules = getConstraints();
        for (final MappedClass c : classes) {
            for (final ClassConstraint v : rules) {
                v.check(mapper, c, ve);
            }
        }

        if (!ve.isEmpty()) {
            final ConstraintViolation worst = ve.iterator().next();
            final ConstraintViolation.Level maxLevel = worst.getLevel();
            if (maxLevel.ordinal() >= ConstraintViolation.Level.FATAL.ordinal()) {
                throw new ConstraintViolationException(ve);
            }

            // sort by class to make it more readable
            final List<LogLine> l = new ArrayList<LogLine>();
            for (final ConstraintViolation v : ve) {
                l.add(new LogLine(v));
            }
            sort(l);

            for (final LogLine line : l) {
                line.log(LOG);
            }
        }
    }

    public static void validateRefactored(final Mapper mapper, final List<MappedClass> classes) {
        final Set<ConstraintViolation> ve = new TreeSet<ConstraintViolation>(new Comparator<ConstraintViolation>() {

            @Override
            public int compare(final ConstraintViolation o1, final ConstraintViolation o2) {
                return o1.getLevel().ordinal() > o2.getLevel().ordinal() ? -1 : 1;
            }
        });

        final List<ClassConstraint> rules = getConstraints();
        for (final MappedClass c : classes) {
            for (final ClassConstraint v : rules) {
                v.check(mapper, c, ve);
            }
        }

        if (!ve.isEmpty()) {
            final ConstraintViolation worst = ve.iterator().next();
            final ConstraintViolation.Level maxLevel = worst.getLevel();
            if (maxLevel.ordinal() >= ConstraintViolation.Level.FATAL.ordinal()) {
                throw new ConstraintViolationException(ve);
            }

            // sort by class to make it more readable
            ve.stream()
              .map(LogLine::new)
              .sorted()
              .forEach(logLine -> logLine.log(LOG));
        }
    }

    private static List<ClassConstraint> getConstraints() {
        final List<ClassConstraint> constraints = new ArrayList<ClassConstraint>(32);

        // normally, i do this with scanning the classpath, but that´d bring
        // another dependency ;)

        // class-level
        constraints.add(new MultipleId());
        constraints.add(new MultipleVersions());
        constraints.add(new NoId());
        constraints.add(new EmbeddedAndId());
        constraints.add(new EntityAndEmbed());
        constraints.add(new EmbeddedAndValue());
        constraints.add(new EntityCannotBeMapOrIterable());
        constraints.add(new org.mongodb.morphia.mapping.validation.classrules.DuplicatedAttributeNames());
        // constraints.add(new ContainsEmbeddedWithId());
        // field-level
        constraints.add(new MisplacedProperty());
        constraints.add(new ReferenceToUnidentifiable());
        constraints.add(new LazyReferenceMissingDependencies());
        constraints.add(new LazyReferenceOnArray());
        constraints.add(new MapKeyDifferentFromString());
        constraints.add(new MapNotSerializable());
        //
        constraints.add(new ContradictingFieldAnnotation(Reference.class, Serialized.class));
        constraints.add(new ContradictingFieldAnnotation(Reference.class, Property.class));
        constraints.add(new ContradictingFieldAnnotation(Reference.class, Embedded.class));
        //
        constraints.add(new ContradictingFieldAnnotation(Embedded.class, Serialized.class));
        constraints.add(new ContradictingFieldAnnotation(Embedded.class, Property.class));
        //
        constraints.add(new ContradictingFieldAnnotation(Property.class, Serialized.class));

        return constraints;
    }
}
