package com.technologicgroup.boost.common.providers;

import com.technologicgroup.boost.common.ContextHolder;
import com.technologicgroup.boost.mock.TestRunnableBean;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class BeanProviderRunnableTest {

  @InjectMocks
  private ContextHolder contextHolder;

  @Mock
  private ApplicationContext context;

  private BeanProviderRunnable<TestRunnableBean> provider;

  @Before
  public void setUp() {
    provider = new BeanProviderRunnable<>(TestRunnableBean.class);
  }

  @Test
  public void testCall_run_OK() {
    when(context.getBean(TestRunnableBean.class)).thenReturn(new TestRunnableBean());
    provider.run();
    verify(context, times(1)).getBean(TestRunnableBean.class);
    Assert.assertNotEquals(0, contextHolder.hashCode());
  }

}
