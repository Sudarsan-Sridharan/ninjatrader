package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.model.entity.Algorithm;
import com.bn.ninjatrader.service.model.CreateAlgorithmRequest;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * @author bradwee2000@gmail.com
 */
public class AlgorithmResourceTest extends AbstractJerseyTest {

  private static final AlgorithmDao algorithmDao = mock(AlgorithmDao.class);

  @Captor
  private ArgumentCaptor<Algorithm> algorithmCaptor;

  @Override
  protected ResourceConfig configureResource(final ResourceConfig resourceConfig) {
    return resourceConfig.register(new AlgorithmResource(algorithmDao));
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

    when(algorithmDao.findByAlgorithmId("algo_id")).thenReturn(Optional.of(algorithm));

    final Response response = target("/algorithms/algo_id").request().get();

    // Verify status code
    assertThat(response.getStatus()).isEqualTo(200);

    // Verify returned algorithm details
    final Algorithm returnedAlgorithm = response.readEntity(Algorithm.class);
    assertThat(returnedAlgorithm).isEqualTo(algorithm);

    // Verify search by id was called
    verify(algorithmDao).findByAlgorithmId("algo_id");
  }

  @Test
  public void testGetNonExistingUser_shouldReturnNotFound() {
    when(algorithmDao.findByAlgorithmId(anyString())).thenReturn(Optional.empty());

    final Response response = target("/algorithms/unknown_id").request().get();

    // Verify status code
    assertThat(response.getStatus()).isEqualTo(404);
  }

  @Test
  public void testPostAlgorithm_shouldInsertOrUpdateAlgorithm() {
    final CreateAlgorithmRequest createRequest = new CreateAlgorithmRequest();
    createRequest.setAlgoId("ABCD");
    createRequest.setDescription("Test Desc");
    createRequest.setAlgorithm("Sample Algorithm");

    final Response response = target("/algorithms").request().post(Entity.json(createRequest));

    // Verify status code
    assertThat(response.getStatus()).isEqualTo(200);

    // Verify Algorithm is saved
    verify(algorithmDao).save(algorithmCaptor.capture());
    final Algorithm algorithm = algorithmCaptor.getValue();
    assertThat(algorithm.getId()).isEqualTo(createRequest.getAlgoId());
    assertThat(algorithm.getDescription()).isEqualTo(createRequest.getDescription());
    assertThat(algorithm.getAlgorithm()).isEqualTo(createRequest.getAlgorithm());
  }

  @Test
  public void testDeleteAlgorithm_shouldDeleteAlgorithm() {
    final Response response = target("/algorithms/123").request().delete();

    // Verify status code
    assertThat(response.getStatus()).isEqualTo(200);

    // Verify delete is called
    verify(algorithmDao).delete("123");
  }
}
