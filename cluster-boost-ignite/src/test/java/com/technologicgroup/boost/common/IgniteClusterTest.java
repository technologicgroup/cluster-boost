package com.technologicgroup.boost.common;

import com.technologicgroup.boost.common.providers.BeanProviderFactory;
import com.technologicgroup.boost.core.ClusterGroup;
import com.technologicgroup.boost.mock.TestClusterJob;
import com.technologicgroup.boost.mock.TestClusterTask;
import com.technologicgroup.boost.mock.TestRunnableBean;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCompute;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.lang.IgniteCallable;
import org.apache.ignite.lang.IgniteRunnable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class IgniteClusterTest {

  private IgniteCluster cluster;

  @Mock
  private Ignite ignite;

  @Mock
  private BeanProviderFactory beanFactory;

  @Mock
  private org.apache.ignite.IgniteCluster igniteCluster;

  @Mock
  private IgniteCompute igniteCompute;

  private String[] hosts;

  @Before
  public void setUp() {
    when(ignite.cluster()).thenReturn(igniteCluster);

    hosts = new String[] { UUID.randomUUID().toString() };
    cluster = new IgniteCluster(ignite, hosts, 10000, beanFactory);
  }

  @Test
  public void testExecute_group_OK() {
    when(ignite.compute(any())).thenReturn(igniteCompute);

    ClusterGroup clusterGroup = new ClusterGroup(Collections.singleton(UUID.randomUUID().toString()));
    cluster.execute(clusterGroup, new TestRunnableBean());

    verify(igniteCompute, times(1)).broadcast(any(IgniteRunnable.class));
  }

  @Test
  public void testExecute_OK() {
    when(ignite.compute()).thenReturn(igniteCompute);
    cluster.execute(new TestRunnableBean());
    verify(igniteCompute, times(1)).broadcast(any(IgniteRunnable.class));
  }

  @Test
  public void testExecuteAsync_group_OK() {
    when(ignite.compute(any())).thenReturn(igniteCompute);

    ClusterGroup clusterGroup = new ClusterGroup(Collections.singleton(UUID.randomUUID().toString()));
    cluster.executeAsync(clusterGroup, new TestRunnableBean());

    verify(igniteCompute, times(1)).broadcastAsync(any(IgniteRunnable.class));
  }

  @Test
  public void testExecuteAsync_OK() {
    when(ignite.compute()).thenReturn(igniteCompute);
    cluster.executeAsync(new TestRunnableBean());
    verify(igniteCompute, times(1)).broadcastAsync(any(IgniteRunnable.class));
  }

  @Test
  public void testExecuteBeanAsync_OK() {
    when(ignite.compute()).thenReturn(igniteCompute);
    when(beanFactory.getRunnable(TestRunnableBean.class)).thenReturn((IgniteRunnable) () -> { });

    cluster.executeBeanAsync(TestRunnableBean.class);
    verify(igniteCompute, times(1)).broadcastAsync(any(IgniteRunnable.class));
    verify(beanFactory, times(1)).getRunnable(TestRunnableBean.class);
  }

  @Test
  public void testExecuteBean_group_OK() {
    ClusterGroup clusterGroup = new ClusterGroup(Collections.singleton(UUID.randomUUID().toString()));

    when(ignite.compute(any())).thenReturn(igniteCompute);
    when(beanFactory.getRunnable(TestRunnableBean.class)).thenReturn((IgniteRunnable) () -> { });

    cluster.executeBean(clusterGroup, TestRunnableBean.class);
    verify(igniteCompute, times(1)).broadcast(any(IgniteRunnable.class));
    verify(beanFactory, times(1)).getRunnable(TestRunnableBean.class);
  }

  @Test
  public void testExecuteBean_OK() {
    when(ignite.compute()).thenReturn(igniteCompute);
    when(beanFactory.getRunnable(TestRunnableBean.class)).thenReturn((IgniteRunnable) () -> { });

    cluster.executeBean(TestRunnableBean.class);
    verify(igniteCompute, times(1)).broadcast(any(IgniteRunnable.class));
    verify(beanFactory, times(1)).getRunnable(TestRunnableBean.class);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testRunBean_OK() {
    when(ignite.compute()).thenReturn(igniteCompute);
    when(beanFactory.getJob(TestClusterJob.class)).thenReturn((IgniteCallable<Integer>) () -> null);

    Collection<Integer> results = cluster.runBean(TestClusterJob.class);
    Assert.assertNotNull(results);
    verify(igniteCompute, times(1)).broadcast(any(IgniteCallable.class));
    verify(beanFactory, times(1)).getJob(TestClusterJob.class);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testRunBean_task_OK() {
    when(ignite.compute()).thenReturn(igniteCompute);
    when(beanFactory.getTask(TestClusterTask.class, "")).thenReturn((IgniteCallable<Integer>) () -> null);

    Collection<Integer> results = cluster.runBean(TestClusterTask.class, "");
    Assert.assertNotNull(results);
    verify(igniteCompute, times(1)).broadcast(any(IgniteCallable.class));
    verify(beanFactory, times(1)).getTask(TestClusterTask.class, "");
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testRunBean_group_task_OK() {
    ClusterGroup clusterGroup = new ClusterGroup(Collections.singleton(UUID.randomUUID().toString()));

    when(ignite.compute(any())).thenReturn(igniteCompute);
    when(beanFactory.getTask(TestClusterTask.class, "")).thenReturn((IgniteCallable<Integer>) () -> null);

    Collection<Integer> results = cluster.runBean(clusterGroup, TestClusterTask.class, "");
    Assert.assertNotNull(results);
    verify(igniteCompute, times(1)).broadcast(any(IgniteCallable.class));
    verify(beanFactory, times(1)).getTask(TestClusterTask.class, "");
  }

  @Test
  public void testGetNodeOrder() {
    ClusterNode node = Mockito.mock(ClusterNode.class);
    when(igniteCluster.localNode()).thenReturn(node);
    when(node.order()).thenReturn(10L);
    Assert.assertEquals(10L, cluster.getNodeOrder());
  }

  @Test
  public void testIsFirstNode() {
    ClusterNode node = Mockito.mock(ClusterNode.class);
    when(igniteCluster.localNode()).thenReturn(node);
    when(node.order()).thenReturn(1L);
    Assert.assertTrue(cluster.isFirstNode());
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testActivate_skipped() {
    ClusterNode node = Mockito.mock(ClusterNode.class);
    when(igniteCluster.localNode()).thenReturn(node);
    when(node.order()).thenReturn(2L);

    cluster.activate();
    verify(igniteCompute, times(0)).broadcast(any(IgniteCallable.class));
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testActivate_OK() {
    ClusterNode node = Mockito.mock(ClusterNode.class);
    when(igniteCluster.localNode()).thenReturn(node);
    when(node.order()).thenReturn(1L);
    when(ignite.compute()).thenReturn(igniteCompute);

    List<Boolean> results = new ArrayList<>();
    for (int i = 0; i < hosts.length; i++) {
      results.add(true);
    }
    when(beanFactory.getJob(ReadyDetectorJob.class)).thenReturn((IgniteCallable<Boolean>) () -> true);
    when(igniteCompute.broadcast(any(IgniteCallable.class))).thenReturn(results);

    cluster.activate();
    verify(igniteCompute, times(1)).broadcast(any(IgniteCallable.class));
  }

}
