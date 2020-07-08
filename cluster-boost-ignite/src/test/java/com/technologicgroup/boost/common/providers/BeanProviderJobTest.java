package com.technologicgroup.boost.common.providers;

import com.technologicgroup.boost.common.ContextHolder;
import com.technologicgroup.boost.mock.TestClusterJob;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import static com.technologicgroup.boost.mock.TestClusterJob.EXCEPTION_CODE;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BeanProviderJobTest {

  @InjectMocks
  private ContextHolder contextHolder;

  @Mock
  private ApplicationContext context;

  private BeanProviderJob<Integer, TestClusterJob> provider;

  @Before
  public void setUp() {
    provider = new BeanProviderJob<>(TestClusterJob.class);
  }

  @Test
  public void testCall_run_OK() {
    when(context.getBean(TestClusterJob.class)).thenReturn(new TestClusterJob(5));
    Integer result = provider.call();

    verify(context, times(1)).getBean(TestClusterJob.class);
    Assert.assertEquals(5, result.longValue());
  }

  @Test
  public void testCall_run_bean_notFound() {
    when(context.getBean(TestClusterJob.class)).thenReturn(null);
    Integer result = provider.call();

    verify(context, times(1)).getBean(TestClusterJob.class);
    Assert.assertNull(result);
    Assert.assertNotEquals(0, contextHolder.hashCode());
  }

  @Test(expected = RuntimeException.class)
  public void testCall_run_exception() {
    when(context.getBean(TestClusterJob.class)).thenReturn(new TestClusterJob(EXCEPTION_CODE));
    provider.call();
  }

}
