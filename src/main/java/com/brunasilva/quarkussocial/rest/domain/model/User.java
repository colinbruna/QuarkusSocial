package com.brunasilva.quarkussocial.rest.domain.model;

import io.quarkus.mongodb.panache.common.MongoEntity;
import lombok.Data;
import org.bson.types.ObjectId;

import javax.persistence.Id;

@MongoEntity(collection = "user")
@Data
public class User{

    @Id
    private ObjectId id;
    private String name;
    private Integer age;
}
