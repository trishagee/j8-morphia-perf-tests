package com.mechanitis.undertest;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;
import org.mongodb.morphia.annotations.Serialized;
import org.mongodb.morphia.mapping.MappedField;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.stream.Stream;

public class Mapper {
    public static Class<? extends Annotation> original(final MappedField mf) {
        Class<? extends Annotation> annType = null;
        for (final Class<? extends Annotation> testType : new Class[]{Property.class, Embedded.class, Serialized.class, Reference.class}) {
            if (mf.hasAnnotation(testType)) {
                annType = testType;
                break;
            }
        }
        return annType;
    }

    public static Class<? extends Annotation> refactored(final MappedField mf) {
        return (Class<? extends Annotation>) Arrays.stream(new Class[]{Property.class, Embedded.class, Serialized.class, Reference.class})
                                                   .filter(mf::hasAnnotation)
                                                   .findFirst()
                                                   .orElse(null);

    }

    public static Class<? extends Annotation> refactoredMore(final MappedField mf) {
        return (Class<? extends Annotation>) Stream.of(Property.class, Embedded.class, Serialized.class, Reference.class)
                                                   .filter(mf::hasAnnotation)
                                                   .findFirst()
                                                   .orElse(null);

    }
}
