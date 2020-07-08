package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.chain.mock.TestClusterTask;
import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.core.ClusterGroup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class ChainTest {

  @Mock
  private Cluster cluster;

  @Test
  public void testOn_OK() {
    ClusterGroup clusterGroup = new ClusterGroup(Collections.singleton(UUID.randomUUID().toString()));

    Chain chain = Chain.of(cluster);
    chain = chain.on(clusterGroup);

    Assert.assertNotNull(chain);
    Assert.assertEquals(clusterGroup, chain.getClusterGroup());
  }

  @Test
  public void testTrack_OK() {
    String trackingId = UUID.randomUUID().toString();
    Chain chain = Chain.of(cluster);
    chain = chain.track(trackingId);

    Assert.assertNotNull(chain);
    Assert.assertEquals(trackingId, chain.getTrackingId());
  }

  @Test
  public void testMap_OK() {
    Chain chain = Chain.of(cluster);
    chain.map(TestClusterTask.class);

    Assert.assertNotNull(chain);
    Assert.assertNull(chain.getArg());
    Assert.assertEquals(1, chain.getSteps().size());
    Assert.assertEquals(TestClusterTask.class, chain.getSteps().get(0).getBean());
  }

  @Test
  public void testMapArg_OK() {
    String arg = UUID.randomUUID().toString();
    Chain chain = Chain.of(cluster);
    chain.map(TestClusterTask.class, arg);

    Assert.assertNotNull(chain);
    Assert.assertEquals(arg, chain.getArg());
    Assert.assertEquals(1, chain.getSteps().size());
    Assert.assertEquals(TestClusterTask.class, chain.getSteps().get(0).getBean());
  }

  @Test
  public void testRunClusterGroup_OK() {
    ClusterGroup clusterGroup = new ClusterGroup(Collections.singleton(UUID.randomUUID().toString()));

    Chain chain = Chain.of(cluster);
    chain.on(clusterGroup);
    chain.run();

    verify(cluster, times(1)).runBean(eq(clusterGroup), eq(ChainBean.class), any());
  }

  @Test
  public void testRunEmptyClusterGroup_OK() {
    ClusterGroup clusterGroup = new ClusterGroup(Collections.emptySet());

    Chain chain = Chain.of(cluster);
    chain.on(clusterGroup);
    Collection<?> result = chain.run();

    verify(cluster, times(0)).runBean(eq(clusterGroup), eq(ChainBean.class), any());
    Assert.assertEquals(0, result.size());
  }

  @Test
  public void testRun_OK() {
    Chain chain = Chain.of(cluster);
    chain.run();

    verify(cluster, times(1)).runBean(eq(ChainBean.class), any());
  }

}
