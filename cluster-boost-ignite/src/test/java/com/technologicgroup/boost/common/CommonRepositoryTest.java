package com.technologicgroup.boost.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.configuration.CacheConfiguration;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import static org.mockito.Mockito.*;

@Slf4j
@RunWith(MockitoJUnitRunner.class)
public class CommonRepositoryTest {

  @InjectMocks
  private ContextHolder contextHolder;

  @Mock
  private CacheConfiguration<Integer, Integer> configuration;

  @Mock
  private IgniteCache<?, ?> cache;

  @Mock
  private Ignite ignite;

  @Mock
  private org.apache.ignite.IgniteCluster igniteCluster;

  @Mock
  private ApplicationContext context;

  @Mock
  private org.apache.ignite.cluster.ClusterGroup igniteClusterGroup;

  private Repository repository;

  public static class Repository extends CommonRepository<Integer, String> { }

  @Before
  @SuppressWarnings("unchecked")
  public void setUp() {
    repository = new Repository();
    when(context.getBean(Ignite.class)).thenReturn(ignite);
    when(ignite.cluster()).thenReturn(igniteCluster);
    when(ignite.cache(any())).thenReturn((IgniteCache<Object, Object>) cache);

    log.info("{}", contextHolder.toString());
    log.info("{}", configuration.toString());
  }

  @Test
  public void testGetName_OK() {
    String name = repository.getName();
    Assert.assertEquals("IntegerStringCache", name);
  }

  @Test
  public void testGetClusterGroup_OK() {
    when(cache.getName()).thenReturn("IntegerStringCache");
    when(igniteCluster.forCacheNodes("IntegerStringCache")).thenReturn(igniteClusterGroup);
    repository.getClusterGroup();
    verify(igniteCluster, times(1)).forCacheNodes("IntegerStringCache");
  }

}
