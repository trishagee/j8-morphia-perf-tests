package com.mechanitis.undertest.entityscanner;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

@Entity
public class B {
    @Id
    private ObjectId id;
}
