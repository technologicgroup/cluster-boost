package com.technologicgroup.boost.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import static org.mockito.Mockito.when;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class ContextHolderTest {

  @InjectMocks
  private ContextHolder contextHolder;

  @Mock
  private Ignite ignite;

  @Mock
  private ApplicationContext context;

  @Mock
  private org.apache.ignite.IgniteCluster igniteCluster;

  private IgniteCluster cluster;

  @Before
  public void setUp() {
    cluster = new IgniteCluster(ignite, null, 0, null);

    log.info("{}", contextHolder.toString());
    log.info("{}", context.toString());
  }

  @Test
  public void testIsReady_true() {
    ContextHolder.cluster = cluster;

    when(ignite.cluster()).thenReturn(igniteCluster);
    when(igniteCluster.active()).thenReturn(true);
    Assert.assertTrue(ContextHolder.isReady());
  }

  @Test
  public void testIsReady_false() {
    ContextHolder.cluster = cluster;

    when(ignite.cluster()).thenReturn(igniteCluster);
    when(igniteCluster.active()).thenReturn(false);
    Assert.assertFalse(ContextHolder.isReady());
  }

  @Test
  public void testIsReady_context_null() {
    ContextHolder.cluster = cluster;
    ContextHolder.context = null;
    Assert.assertFalse(ContextHolder.isReady());
  }

  @Test
  public void testIsReady_cluster_null() {
    ContextHolder.cluster = null;
    Assert.assertFalse(ContextHolder.isReady());
  }

}
