package com.technologicgroup.boost.common;

import com.technologicgroup.boost.common.providers.BeanProviderFactory;
import org.apache.ignite.Ignite;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import java.util.UUID;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReadyDetectorJobTest {

  @Mock
  private ApplicationContext context;

  @Mock
  private Ignite ignite;

  @Mock
  private BeanProviderFactory beanFactory;

  @Mock
  private org.apache.ignite.IgniteCluster igniteCluster;

  private ReadyDetectorJob job;

  @Before
  public void setUp() {
    String[] hosts = new String[] { UUID.randomUUID().toString() };
    IgniteCluster cluster = new IgniteCluster(ignite, hosts, 10000, beanFactory);

    when(ignite.cluster()).thenReturn(igniteCluster);

    ContextHolder.cluster = cluster;
    ContextHolder.context = context;

    job = new ReadyDetectorJob();
  }

  @Test
  public void testRun_active() {
    when(igniteCluster.active()).thenReturn(true);
    Assert.assertTrue(job.run());
  }

  @Test
  public void testRun_not_active() {
    when(igniteCluster.active()).thenReturn(false);
    Assert.assertFalse(job.run());
  }

}
