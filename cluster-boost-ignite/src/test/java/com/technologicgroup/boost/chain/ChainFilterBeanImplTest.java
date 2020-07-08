package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.core.Cluster;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ChainFilterBeanImplTest {

  @Mock
  private Cluster cluster;

  @InjectMocks
  private ChainFilterBeanImpl<Integer> chainFilter;

  @Test
  public void testRun_filtered_OK() {
    String nodeId = UUID.randomUUID().toString();
    Integer nodeResult = 100;

    Collection<ChainResult<Integer>> chainResults = new ArrayList<>();
    chainResults.add(new ChainResult<>(nodeId, nodeResult));

    when(cluster.getLocalNode()).thenReturn(nodeId);
    Integer result = chainFilter.run(chainResults);

    verify(cluster, times(1)).getLocalNode();
    Assert.assertEquals(nodeResult, result);
  }

  @Test
  public void testRun_filtered_Null() {
    String nodeId = UUID.randomUUID().toString();
    Integer nodeResult = 100;

    Collection<ChainResult<Integer>> chainResults = new ArrayList<>();
    chainResults.add(new ChainResult<>(nodeId, nodeResult));

    when(cluster.getLocalNode()).thenReturn(UUID.randomUUID().toString());
    Integer result = chainFilter.run(chainResults);

    verify(cluster, times(1)).getLocalNode();
    Assert.assertNull(result);
  }

}
