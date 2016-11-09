package com.mechanitis.undertest;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.mapping.MappedClass;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.Mapper;

import java.util.Arrays;
import java.util.List;

import static org.mongodb.morphia.query.QueryImpl.parseFieldsString;

public class DatastoreImpl extends org.mongodb.morphia.DatastoreImpl {
    private Mapper mapper;

    public DatastoreImpl(Morphia morphia, MongoClient mongoClient, String dbName) {
        super(morphia, mongoClient, dbName);
    }

    public DatastoreImpl(Morphia morphia, Mapper mapper, MongoClient mongoClient, String dbName) {
        super(morphia, mapper, mongoClient, dbName);
        this.mapper = new Mapper();
    }

    public DatastoreImpl(Mapper mapper) {
        super(null, null, null);
        this.mapper = mapper;
    }

    @SuppressWarnings("deprecation")
    //Only used for ensureIndex
    public void processClassAnnotationsOriginal(final DBCollection dbColl, final MappedClass mc, final boolean background,
                                                final List<MappedClass> parentMCs, final List<MappedField> parentMFs) {
        // Ensure indexes from class annotation
        // TG: since we're getting this off a mapped class and the annotation is class level, I expect no more than one in this list
        final List<Indexes> indexes = mc.getAnnotations(Indexes.class);
        if (indexes != null) {
            for (final Indexes idx : indexes) {
                if (idx.value().length > 0) {
                    for (final Index index : idx.value()) {
                        if (index.fields().length != 0) {
                            ensureIndex(mc, dbColl, index.fields(), index.options(), background, parentMCs, parentMFs);
                        } else {
                            final BasicDBObject fields = parseFieldsString(index.value(), mc.getClazz(), mapper,
                                    !index.disableValidation(), parentMCs, parentMFs);
                            ensureIndex(dbColl, index.name(), fields, index.unique(), index.dropDups(),
                                    index.background() ? index.background()
                                                       : background, index.sparse(), index.expireAfterSeconds());
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    //Only used for ensureIndex
    public void processClassAnnotationsRefactored(final DBCollection dbColl, final MappedClass mc, final boolean background,
                                                  final List<MappedClass> parentMCs, final List<MappedField> parentMFs) {
        // Ensure indexes from class annotation
        // TG: since we're getting this off a mapped class and the annotation is class level, in reality I expect no more than one in this
        // list
        final List<Indexes> indexes = mc.getAnnotations(Indexes.class);
        if (indexes != null) {
            indexes.stream()
                   .filter(idx -> idx.value().length > 0)
                   .forEach(idx -> {
                       for (final Index index : idx.value()) {
                           if (index.fields().length != 0) {
                               ensureIndex(mc, dbColl, index.fields(), index.options(), background, parentMCs, parentMFs);
                           } else {
                               final BasicDBObject fields = parseFieldsString(index.value(), mc.getClazz(), mapper,
                                                                              !index.disableValidation(), parentMCs,
                                                                              parentMFs);
                               ensureIndex(dbColl, index.name(), fields, index.unique(), index.dropDups(),
                                           index.background() ? index.background()
                                                   : background, index.sparse(), index.expireAfterSeconds());
                           }
                       }
                   });
        }
    }


    @SuppressWarnings("deprecation")
    //Only used for ensureIndex
    public void processClassAnnotationsRefactoredMore(final DBCollection dbColl, final MappedClass mc, final boolean
            background,
                                                      final List<MappedClass> parentMCs, final List<MappedField>
                                                                  parentMFs) {
        // Ensure indexes from class annotation
        final List<Indexes> indexes = mc.getAnnotations(Indexes.class);
        if (indexes != null) {
            indexes.stream()
                   .filter(idx -> idx.value().length > 0)
                   .flatMap(idx -> Arrays.stream(idx.value()))
                   .forEach(index -> {
                       if (index.fields().length != 0) {
                           ensureIndex(mc, dbColl, index.fields(), index.options(), background, parentMCs, parentMFs);
                       } else {
                           final BasicDBObject fields = parseFieldsString(index.value(), mc.getClazz(), mapper,
                                                                          !index.disableValidation(), parentMCs,
                                                                          parentMFs);
                           ensureIndex(dbColl, index.name(), fields, index.unique(), index.dropDups(),
                                       index.background() ? index.background() : background, index.sparse(), index
                                               .expireAfterSeconds());
                       }
                   });
        }
    }
}
