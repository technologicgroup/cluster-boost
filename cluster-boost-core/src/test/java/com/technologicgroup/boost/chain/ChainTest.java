package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.chain.mock.TestClusterTask;
import com.technologicgroup.boost.core.Cluster;
import com.technologicgroup.boost.core.ClusterGroup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;
import java.util.UUID;

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
  public void testRun_OK() {

  }

}
