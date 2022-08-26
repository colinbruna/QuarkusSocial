package com.brunasilva.quarkussocial.rest;

import com.brunasilva.quarkussocial.rest.domain.model.User;
import com.brunasilva.quarkussocial.rest.domain.repository.UserRepository;
import com.brunasilva.quarkussocial.rest.dto.CreateUserRequest;
import com.brunasilva.quarkussocial.rest.dto.ResponseError;
import io.quarkus.mongodb.panache.PanacheQuery;
import org.bson.types.ObjectId;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    private final UserRepository repository;
    private final Validator validator;

    @Inject
    public UserResource(final UserRepository repository, final Validator validator) {
        this.repository = repository;
        this.validator = validator;
    }

    @POST
    public Response createUser(CreateUserRequest userRequest) {

        Set<ConstraintViolation<CreateUserRequest>> violations = validator.validate(userRequest);
        if(!violations.isEmpty()){
            ResponseError responseError = ResponseError.createFromValidation(violations);
            return Response.status(400).entity(responseError).build();
        }

        User user = new User();
        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());

        repository.persist(user);

        return Response.ok(user).build();
    }

    @GET
    public Response listAllUsers() {
        PanacheQuery<User> query = repository.findAll();
        return Response.ok(query.list()).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") ObjectId id) {
        User user = repository.findById(id);

        if (user != null) {
            repository.delete(user);
            return Response.ok().build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }

    @PUT
    @Path("{id}")
    public Response updateUser(@PathParam("id") ObjectId id, CreateUserRequest userData) {
        User user = repository.findById(id);

        if (user != null) {
            user.setName(userData.getName());
            user.setAge(userData.getAge());

            repository.update(user);

            return Response.ok(user).build();
        }

        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
