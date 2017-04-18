package com.bn.ninjatrader.service.resource;

import com.bn.ninjatrader.model.dao.AlgorithmDao;
import com.bn.ninjatrader.model.entity.TradeAlgorithm;
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
  private ArgumentCaptor<TradeAlgorithm> algorithmCaptor;

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
    final TradeAlgorithm algorithm = TradeAlgorithm.builder()
        .algoId("algo_id").userId("user_id").algorithm("sample algo").description("desc").build();

    when(algorithmDao.findByTradeAlgorithmId("algo_id")).thenReturn(Optional.of(algorithm));

    final Response response = target("/algorithms/algo_id").request().get();

    // Verify status code
    assertThat(response.getStatus()).isEqualTo(200);

    // Verify returned algorithm details
    final TradeAlgorithm returnedAlgorithm = response.readEntity(TradeAlgorithm.class);
    assertThat(returnedAlgorithm).isEqualTo(algorithm);

    // Verify search by id was called
    verify(algorithmDao).findByTradeAlgorithmId("algo_id");
  }

  @Test
  public void testGetNonExistingUser_shouldReturnNotFound() {
    when(algorithmDao.findByTradeAlgorithmId(anyString())).thenReturn(Optional.empty());

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
    final TradeAlgorithm algorithm = algorithmCaptor.getValue();
    assertThat(algorithm.getId()).isEqualTo(createRequest.getAlgoId());
    assertThat(algorithm.getDescription()).isEqualTo(createRequest.getDescription());
    assertThat(algorithm.getAlgorithm()).isEqualTo(createRequest.getAlgorithm());
  }
}
