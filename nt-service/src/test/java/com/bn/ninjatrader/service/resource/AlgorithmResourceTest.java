package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.common.model.Algorithm;
import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.service.model.CreateAlgorithmRequest;
import com.bn.ninjatrader.service.security.AuthenticatedUser;
import com.bn.ninjatrader.service.security.NtSecurityContext;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.client.Entity;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Optional;

import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.Status.OK;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author bradwee2000@gmail.com
 */
public class AlgorithmResourceTest extends AbstractJerseyTest {

  private static final AlgorithmDao algorithmDao = mock(AlgorithmDao.class, Answers.RETURNS_MOCKS);

  @Captor
  private ArgumentCaptor<Algorithm> algorithmCaptor;

  @Override
  protected ResourceConfig configureResource(final ResourceConfig resourceConfig) {
    return resourceConfig.register(new AlgorithmResource(algorithmDao)).register(DummyAuthFilter.class);
  }

  @Before
  public void before() {
    initMocks(this);
    reset(algorithmDao);
  }

  @Test
  public void testGetExistingAlgorithm_shouldReturnAlgorithm() {
    final Algorithm algorithm = Algorithm.builder()
        .algoId("algo_id").userId("user_id").algorithm("sample algo").description("desc").build();

    when(algorithmDao.findOneByAlgorithmId("algo_id")).thenReturn(Optional.of(algorithm));

    final Response response = target("/algorithms/algo_id").request().get();

    // Verify status code
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

    // Verify returned algorithm details
    final Algorithm returnedAlgorithm = response.readEntity(Algorithm.class);
    assertThat(returnedAlgorithm).isEqualTo(algorithm);

    // Verify search by id was called
    verify(algorithmDao).findOneByAlgorithmId("algo_id");
  }

  @Test
  public void testGetNonExistingUser_shouldReturnNotFound() {
    when(algorithmDao.findOneByAlgorithmId(anyString())).thenReturn(Optional.empty());

    final Response response = target("/algorithms/unknown_id").request().get();

    // Verify status code
    assertThat(response.getStatus()).isEqualTo(NOT_FOUND.getStatusCode());
  }

  @Test
  public void testGetAllAlgorithmsForUser_shouldReturnAllOfUsersAlgorithms() {
    final AlgorithmDao.FindAlgorithmsOperation findOperation =
        mock(AlgorithmDao.FindAlgorithmsOperation.class, Answers.RETURNS_SELF);
    when(algorithmDao.findAlgorithms()).thenReturn(findOperation);

    final Response response = target("/algorithms").request().get();

    // Verify status code
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

    // Verify correct userId was userd
    verify(findOperation).withUserId("USER_ID");
  }

  @Test
  public void testPostAlgorithm_shouldInsertOrUpdateAlgorithm() {
    when(algorithmDao.save(any(Algorithm.class))).thenReturn(Algorithm.builder().algoId("SAVED_ALGO_ID").build());

    final CreateAlgorithmRequest createRequest = new CreateAlgorithmRequest();
    createRequest.setAlgoId("ABCD");
    createRequest.setDescription("Test Desc");
    createRequest.setAlgorithm("Sample Algorithm");

    final Response response = target("/algorithms").request().post(Entity.json(createRequest));

    // Verify status code
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

    // Verify Algorithm is saved
    verify(algorithmDao).save(algorithmCaptor.capture());
    final Algorithm algorithm = algorithmCaptor.getValue();
    assertThat(algorithm.getId()).isEqualTo(createRequest.getAlgoId());
    assertThat(algorithm.getUserId()).isEqualTo("USER_ID");
    assertThat(algorithm.getDescription()).isEqualTo(createRequest.getDescription());
    assertThat(algorithm.getAlgorithm()).isEqualTo(createRequest.getAlgorithm());
  }

  @Test
  public void testDeleteAlgorithm_shouldDeleteAlgorithm() {
    final Response response = target("/algorithms/123").request().delete();

    // Verify status code
    assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());

    // Verify delete is called
    verify(algorithmDao).delete("123");
  }

  @Provider
  @Priority(Priorities.AUTHENTICATION)
  public static final class DummyAuthFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
      final AuthenticatedUser user = new AuthenticatedUser("USER_ID", "John", "Doe", null);
      requestContext.setSecurityContext(new NtSecurityContext(user, ""));
    }
  }
}
