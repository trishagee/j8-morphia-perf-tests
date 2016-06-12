package com.mechanitis.undertest;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.mapping.MappedClass;
import org.mongodb.morphia.mapping.MappedField;
import org.mongodb.morphia.mapping.Mapper;

import java.util.ArrayList;
import java.util.List;

import static org.mongodb.morphia.query.QueryImpl.parseFieldsString;

public class DatastoreImpl extends org.mongodb.morphia.DatastoreImpl {
    private Mapper mapper = new Mapper();

    public DatastoreImpl(Morphia morphia, MongoClient mongoClient, String dbName) {
        super(morphia, mongoClient, dbName);
    }

    public DatastoreImpl(Morphia morphia, Mapper mapper, MongoClient mongoClient, String dbName) {
        super(morphia, mapper, mongoClient, dbName);
    }

    @SuppressWarnings("deprecation")
    //Only used for ensureIndex
    public void processClassAnnotationsOriginal(final String collectionName, final MappedClass mc) {
        List<MappedClass> parentMCs = new ArrayList<>();
        List<MappedField> parentMFs = new ArrayList<>();
        // Ensure indexes from class annotation
        // TG: since we're getting this off a mapped class and the annotation is class level, I expect no more than one in this list
        final List<Indexes> indexes = mc.getAnnotations(Indexes.class);
        if (indexes != null) {
            for (final Indexes idx : indexes) {
                if (idx.value().length > 0) {
                    for (final Index index : idx.value()) {
                        if (index.fields().length != 0) {
                            ensureIndex(mc, collectionName, index.fields(), index.options(), true, parentMCs, parentMFs);
                        } else {
                            final BasicDBObject fields = parseFieldsString(index.value(), mc.getClazz(), mapper,
                                    !index.disableValidation(), parentMCs, parentMFs);
                            ensureIndex(collectionName, index.name(), fields, index.unique(), index.dropDups(),
                                    index.background() ? index.background() : true, index.sparse(), index.expireAfterSeconds());
                        }
                    }
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    //Only used for ensureIndex
    public void processClassAnnotationsRefactored(final String collectionName, final MappedClass mc) {
        List<MappedClass> parentMCs = new ArrayList<>();
        List<MappedField> parentMFs = new ArrayList<>();
        // Ensure indexes from class annotation
        // TG: since we're getting this off a mapped class and the annotation is class level, I expect no more than one in this list
        final List<Indexes> indexes = mc.getAnnotations(Indexes.class);
        if (indexes != null) {
            indexes.stream()
                   .filter(idx -> idx.value().length > 0)
                   .forEach(idx -> {
                       for (final Index index : idx.value()) {
                           if (index.fields().length != 0) {
                               ensureIndex(mc, collectionName, index.fields(), index.options(), true, parentMCs, parentMFs);
                           } else {
                               final BasicDBObject fields = parseFieldsString(index.value(), mc.getClazz(), mapper,
                                       !index.disableValidation(), parentMCs, parentMFs);
                               ensureIndex(collectionName, index.name(), fields, index.unique(), index.dropDups(),
                                       index.background() ? index.background() : true, index.sparse(), index.expireAfterSeconds());
                           }
                       }
                   });
        }
    }

    // STUBS
    // This is actually a bit crap, since ensureIndex is a database call and almost definitely the most expensive call in the above method
    private void ensureIndex(String dbColl, String name, BasicDBObject fields, boolean unique, boolean b, boolean b1, boolean sparse, int i) {
        System.out
                .println("dbColl = [" + dbColl + "], name = [" + name + "], fields = [" + fields + "], unique = [" + unique + "], b = [" + b + "], b1 = [" + b1 + "], sparse = [" + sparse + "], i = [" + i + "]");
    }

    private void ensureIndex(MappedClass mc, String dbColl, Field[] fields, IndexOptions options, boolean background, List<MappedClass>
            parentMCs, List<MappedField> parentMFs) {
        System.out
                .println("mc = [" + mc + "], dbColl = [" + dbColl + "], fields = [" + fields + "], options = [" + options + "], background = [" + background + "], parentMCs = [" + parentMCs + "], parentMFs = [" + parentMFs + "]");
    }

}
