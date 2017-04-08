package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.model.dao.UserDao;
import com.bn.ninjatrader.model.entity.User;
import com.bn.ninjatrader.model.entity.UserFactory;
import com.bn.ninjatrader.service.model.CreateUserRequest;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

/**
 * @author bradwee2000@gmail.com
 */
public class UserResourceTest extends AbstractJerseyTest {

  private static final UserDao userDao = mock(UserDao.class);
  private static final UserFactory userFactory = mock(UserFactory.class);

  @Override
  protected ResourceConfig configureResource(final ResourceConfig resourceConfig) {
    return resourceConfig.register(new UserResource(userDao, userFactory));
  }

  @Before
  public void before() {
    reset(userDao);
    reset(userFactory);
  }

  @Test
  public void testGetExistingUser_shouldReturnUser() {
    final User user = User.builder().userId("testId").username("jd")
        .firstname("john").lastname("doe").email("jd@e.com").build();

    when(userDao.findByUserId(anyString())).thenReturn(Optional.of(user));

    final Response response = target("/users/testId").request().get();

    // Verify status code
    assertThat(response.getStatus()).isEqualTo(200);

    // Verify returned user details
    final User returnedUser = response.readEntity(User.class);
    assertThat(returnedUser).isEqualTo(user);

    // Verify search by id was called
    verify(userDao).findByUserId("testId");
  }

  @Test
  public void testGetNonExistingUser_shouldReturnNotFound() {
    when(userDao.findByUserId(anyString())).thenReturn(Optional.empty());

    final Response response = target("/users/unknown").request().get();

    // Verify status code
    assertThat(response.getStatus()).isEqualTo(404);
  }

  @Test
  public void testCreateUser_shouldCreateNewUser() {
    when(userFactory.create()).thenReturn(User.builder().userId("testId"));

    final CreateUserRequest request = new CreateUserRequest();
    request.setUsername("jd");
    request.setFirstname("John");
    request.setLastname("Doe");
    request.setEmail("jd.email.com");

    final Response response = target("/users").request().post(Entity.json(request));

    // Verify status code
    assertThat(response.getStatus()).isEqualTo(200);

    // Verify returned User
    final User user = response.readEntity(User.class);
    assertThat(user.getUserId()).isEqualTo("testId");
    assertThat(user.getUsername()).isEqualTo("jd");
    assertThat(user.getFirstname()).isEqualTo("John");
    assertThat(user.getLastname()).isEqualTo("Doe");
    assertThat(user.getEmail()).isEqualTo("jd.email.com");

    verify(userDao).saveUser(user);
  }
}
