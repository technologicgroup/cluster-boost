package com.technologicgroup.boost.audit.providers;

import com.technologicgroup.boost.audit.AuditItem;
import com.technologicgroup.boost.audit.AuditItemAccessor;
import com.technologicgroup.boost.common.ContextHolder;
import com.technologicgroup.boost.mock.TestFailRunnableBean;
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
public class BeanProviderAuditRunnableTest {

  @SuppressWarnings("unused")
  @InjectMocks
  private ContextHolder contextHolder;

  @Mock
  private ApplicationContext context;

  @Mock
  private AuditItemAccessor auditItemAccessor;

  private BeanProviderAuditRunnable<?> provider;

  @Before
  public void setUp() {
    provider = new BeanProviderAuditRunnable<>(TestRunnableBean.class);
  }

  @Test
  public void testCall_run_OK() {
    when(context.getBean(TestRunnableBean.class)).thenReturn(new TestRunnableBean());
    when(context.getBean(AuditItemAccessor.class)).thenReturn(auditItemAccessor);

    provider.run();

    verify(context, times(1)).getBean(TestRunnableBean.class);
    verify(auditItemAccessor, times(1)).put(any(String.class), any(AuditItem.class));
  }

  @Test(expected = RuntimeException.class)
  public void testRun_bean_notFound() {
    when(context.getBean(TestRunnableBean.class)).thenReturn(null);
    provider.run();
  }

  @Test
  public void testRun_exception_tracked() {
    provider = new BeanProviderAuditRunnable<>(TestFailRunnableBean.class);
    when(context.getBean(TestFailRunnableBean.class)).thenReturn(new TestFailRunnableBean());
    when(context.getBean(AuditItemAccessor.class)).thenReturn(auditItemAccessor);

    try {
      provider.run();
      Assert.fail();
    } catch (Exception e) {
      Assert.assertEquals(TestFailRunnableBean.MESSAGE, e.getMessage());
    }
    verify(auditItemAccessor, times(1)).put(any(String.class), any(AuditItem.class));
  }

}
