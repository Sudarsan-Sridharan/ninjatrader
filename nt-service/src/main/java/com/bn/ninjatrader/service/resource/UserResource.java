package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.model.dao.UserDao;
import com.bn.ninjatrader.model.entity.User;
import com.bn.ninjatrader.model.entity.UserFactory;
import com.bn.ninjatrader.service.model.CreateUserRequest;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author bradwee2000@gmail.com
 */
@Singleton
@Path("/users")
public class UserResource {
  private static final Logger LOG = LoggerFactory.getLogger(UserResource.class);

  private final UserDao userDao;
  private final UserFactory userFactory;

  @Inject
  public UserResource(final UserDao userDao, final UserFactory userFactory) {
    this.userDao = userDao;
    this.userFactory = userFactory;
  }

  @GET
  @Path("/{userId}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getUser(@PathParam("userId") final String userId) {
    final User user = userDao.findByUserId(userId).orElseThrow(() -> new NotFoundException("userId not found"));
    return Response.ok(user).build();
  }

  @POST
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response createUser(final CreateUserRequest req) {
    final User user = userFactory.create()
        .username(req.getUsername())
        .firstname(req.getFirstname())
        .lastname(req.getLastname())
        .email(req.getEmail()).build();

    userDao.saveUser(user);

    return Response.ok(user).build();
  }
}