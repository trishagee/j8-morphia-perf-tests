package com.mechanitis.undertest;

import org.mongodb.morphia.mapping.MappedField;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class MappedClass {
    private final List<MappedField> persistenceFields = new ArrayList<>();

    public void addField(MappedField field) {
        persistenceFields.add(field);
    }

    public List<MappedField> getFieldsAnnotatedWithOriginal(final Class<? extends Annotation> clazz) {
        final List<MappedField> results = new ArrayList<MappedField>();
        for (final MappedField mf : persistenceFields) {
            if (mf.getAnnotations().containsKey(clazz)) {
                results.add(mf);
            }
        }
        return results;
    }

    public List<MappedField> getFieldsAnnotatedWithRefactored(final Class<? extends Annotation> clazz) {
        return persistenceFields.stream()
                                .filter(mf -> mf.getAnnotations().containsKey(clazz))
                                .collect(toList());
    }

}
