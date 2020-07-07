package com.technologicgroup.boost.chain;

import com.technologicgroup.boost.chain.mock.TestChainBean;
import com.technologicgroup.boost.chain.mock.TestClusterTask;
import com.technologicgroup.boost.chain.mock.TestFailClusterTask;
import com.technologicgroup.boost.core.Cluster;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class ChainBeanTest {

  @Mock
  private Cluster cluster;

  @Mock
  private Chain chain;

  private CountDownLatch startLatch;

  private CountDownLatch finishLatch;

  @Before
  public void setUp() {
    startLatch = new CountDownLatch(1);
    finishLatch = new CountDownLatch(1);
  }

  @Test
  public void testRun_OK() {
    String trackingId = UUID.randomUUID().toString();
    String localNode = UUID.randomUUID().toString();
    String arg = UUID.randomUUID().toString();

    List<ChainStep<?, ?>> items = new ArrayList<>();
    items.add(new ChainStep<>(TestClusterTask.class, chain));

    when(cluster.getLocalNode()).thenReturn(localNode);

    TestChainBean chainBean = new TestChainBean(cluster, startLatch, finishLatch);
    ChainResult<Integer> result = chainBean.run(new ChainArgument<>(arg, items, trackingId));

    Assert.assertEquals(localNode, result.getNodeId());
    Assert.assertEquals(arg.length(), result.getResult().longValue());
    Assert.assertEquals(0, startLatch.getCount());
    Assert.assertEquals(0, finishLatch.getCount());
  }

  @Test
  public void testRun_Failure() {
    String trackingId = UUID.randomUUID().toString();
    String arg = UUID.randomUUID().toString();

    List<ChainStep<?, ?>> items = new ArrayList<>();
    items.add(new ChainStep<>(TestFailClusterTask.class, chain));

    TestChainBean chainBean = new TestChainBean(cluster, startLatch, finishLatch);
    try {
      chainBean.run(new ChainArgument<>(arg, items, trackingId));
      Assert.fail();
    } catch (Exception e) {
      Assert.assertEquals("Failure", e.getMessage());
    }

    verify(cluster, times(0)).getLocalNode();
    Assert.assertEquals(0, startLatch.getCount());
    Assert.assertEquals(0, finishLatch.getCount());
  }

}
