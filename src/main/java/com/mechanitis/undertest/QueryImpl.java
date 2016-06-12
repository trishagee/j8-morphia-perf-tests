package com.mechanitis.undertest;

import org.mongodb.morphia.mapping.MappedClass;
import org.mongodb.morphia.mapping.MappedField;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class QueryImpl<T> {
    private boolean includeFields;

    public String[] retrieveKnownFieldsOriginal(org.mongodb.morphia.DatastoreImpl ds, Class clazz) {
        final MappedClass mc = ds.getMapper().getMappedClass(clazz);
        final List<String> fields = new ArrayList<String>(mc.getPersistenceFields().size() + 1);
        for (final MappedField mf : mc.getPersistenceFields()) {
            fields.add(mf.getNameToStore());
        }
        return fields.toArray(new String[fields.size()]);
    }

    public String[] retrieveKnownFieldsSimplified(org.mongodb.morphia.DatastoreImpl ds, Class clazz) {
        final MappedClass mc = ds.getMapper().getMappedClass(clazz);
        final List<String> fields = new ArrayList<>();
        for (final MappedField mf : mc.getPersistenceFields()) {
            fields.add(mf.getNameToStore());
        }
        return fields.toArray(new String[0]);
    }

    public String[] retrieveKnownFieldsRefactored(org.mongodb.morphia.DatastoreImpl ds, Class clazz) {
        final MappedClass mc = ds.getMapper().getMappedClass(clazz);
        return mc.getPersistenceFields()
                 .stream()
                 .map(MappedField::getNameToStore)
                 .collect(Collectors.toList())
                 .toArray(new String[0]);
    }

}
