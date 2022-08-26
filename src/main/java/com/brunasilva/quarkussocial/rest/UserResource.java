package com.brunasilva.quarkussocial.rest;

import com.brunasilva.quarkussocial.rest.domain.model.User;
import com.brunasilva.quarkussocial.rest.dto.CreateUserRequest;
import io.quarkus.mongodb.panache.PanacheQuery;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserResource {

    @POST
    public Response createUser(CreateUserRequest userRequest) {
        User user = new User();
        user.setAge(userRequest.getAge());
        user.setName(userRequest.getName());

        user.persist();

        return Response.ok(user).build();
    }

    @GET
    public Response listAllUsers() {
        PanacheQuery<User> query = User.findAll();
        return Response.ok(query.list()).build();
    }

}
