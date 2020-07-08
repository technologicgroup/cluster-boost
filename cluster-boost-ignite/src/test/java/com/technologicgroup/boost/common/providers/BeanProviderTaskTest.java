package com.technologicgroup.boost.common.providers;

import com.technologicgroup.boost.common.ContextHolder;
import com.technologicgroup.boost.mock.TestClusterTask;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BeanProviderTaskTest {
  @InjectMocks
  private ContextHolder contextHolder;

  @Mock
  private ApplicationContext context;

  private BeanProviderTask<String, Integer, TestClusterTask> provider;

  @Test
  public void testCall_run_OK() {
    provider = new BeanProviderTask<>(TestClusterTask.class, "10");

    when(context.getBean(TestClusterTask.class)).thenReturn(new TestClusterTask());
    Integer result = provider.call();

    verify(context, times(1)).getBean(TestClusterTask.class);
    Assert.assertEquals(10, result.longValue());
  }

  @Test
  public void testCall_run_bean_notFound() {
    provider = new BeanProviderTask<>(TestClusterTask.class, "10");

    when(context.getBean(TestClusterTask.class)).thenReturn(null);
    Integer result = provider.call();

    verify(context, times(1)).getBean(TestClusterTask.class);
    Assert.assertNull(result);
    Assert.assertNotEquals(0, contextHolder.hashCode());
  }

  @Test(expected = RuntimeException.class)
  public void testCall_run_exception() {
    provider = new BeanProviderTask<>(TestClusterTask.class, null);

    when(context.getBean(TestClusterTask.class)).thenReturn(new TestClusterTask());
    provider.call();
  }

}
