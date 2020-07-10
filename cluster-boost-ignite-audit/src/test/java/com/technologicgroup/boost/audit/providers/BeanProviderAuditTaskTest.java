package com.technologicgroup.boost.audit.providers;

import com.technologicgroup.boost.audit.AuditItem;
import com.technologicgroup.boost.audit.AuditItemAccessor;
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
public class BeanProviderAuditTaskTest {

  @SuppressWarnings("unused")
  @InjectMocks
  private ContextHolder contextHolder;

  @Mock
  private ApplicationContext context;

  @Mock
  private AuditItemAccessor auditItemAccessor;

  private BeanProviderAuditTask<String, Integer, TestClusterTask> provider;

  @Test
  public void testRun_OK() {
    provider = new BeanProviderAuditTask<>(TestClusterTask.class, "10");
    when(context.getBean(TestClusterTask.class)).thenReturn(new TestClusterTask());
    Integer result = provider.call();

    verify(context, times(1)).getBean(TestClusterTask.class);
    Assert.assertEquals(10, result.longValue());
  }

  @Test(expected = RuntimeException.class)
  public void testCall_run_bean_notFound() {
    provider = new BeanProviderAuditTask<>(TestClusterTask.class, "10");
    when(context.getBean(TestClusterTask.class)).thenReturn(null);
    provider.call();
  }

  @Test(expected = RuntimeException.class)
  public void testCall_run_exception() {
    provider = new BeanProviderAuditTask<>(TestClusterTask.class, null);
    when(context.getBean(TestClusterTask.class)).thenReturn(new TestClusterTask());
    provider.call();
  }

  @Test
  public void testCall_run_exception_tracked() {
    provider = new BeanProviderAuditTask<>(TestClusterTask.class, null);
    when(context.getBean(TestClusterTask.class)).thenReturn(new TestClusterTask());
    when(context.getBean(AuditItemAccessor.class)).thenReturn(auditItemAccessor);

    try {
      provider.call();
      Assert.fail();
    } catch (Exception e) {
      Assert.assertEquals(TestClusterTask.MESSAGE, e.getMessage());
    }
    verify(auditItemAccessor, times(1)).put(any(String.class), any(AuditItem.class));
  }

}
