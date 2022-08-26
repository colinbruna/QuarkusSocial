package com.brunasilva.quarkussocial.rest.domain.repository;

import com.brunasilva.quarkussocial.rest.domain.model.User;
import io.quarkus.mongodb.panache.PanacheMongoRepository;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheMongoRepository<User> {
}
